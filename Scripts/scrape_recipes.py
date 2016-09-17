""" once we have a good index of recipes, let's parse them """


import requests
import json
from bs4 import BeautifulSoup
from settings import start_urls, base_domain
from multiprocessing.dummy import Pool as ThreadPool

def main():
    with open('more_recipes_2.txt', 'r') as infile:
        with open('big_ole_json.txt', 'w') as outfile:
            for line in infile:
                print('dumped '+line.strip())
                outfile.write(json.dumps(extract_recipe(line.strip()))+'\n\n')


def extract_recipe(url):
    r = requests.get('http://'+base_domain+url)
    soup = BeautifulSoup(r.text, 'html.parser')

    name = soup.find('h1', attrs={'itemprop': 'name'}).text
    image = soup.find('img', attrs={'itemprop':'image'})['src']
    thumb = image.replace('/720x405/', '/250x250/')
    cook_time = soup.find('span', {'class':'ready-in-time'})
    if cook_time is not None:
        cook_time = cook_time.text
    else:
        cook_time = "None"
    
    ingredients = [] 
    for thing in soup.find_all('span', attrs={'itemprop': 'ingredients'}):
        if thing is not None:
            ingredients.append(thing.text)

    steps = []
    for step in soup.find_all('span', attrs={'class': 'recipe-directions__list--item'}):
        if step is not None:
            steps.append(step.text)

    recipe = {'name': name,
              'image_url': image,
              'thumb_url': thumb,
              'cook_time': cook_time,
              'ingredients': ingredients,
              'steps': steps
              }

    return recipe


def yield_from_file(filename):
    with open(filename, 'r') as f:
        for line in f:
            yield line.strip()

if __name__ == "__main__":
    main()