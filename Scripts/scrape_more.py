""" expands recipes via related """

import requests
from bs4 import BeautifulSoup
from settings import start_urls, base_domain
from multiprocessing.dummy import Pool as ThreadPool

def main():
    with open('more_recipes.txt', 'w') as f:
        pool = ThreadPool(20)
        results = pool.map(yield_related, yield_recipes('recipe_urls.txt'))
        
        for result in results:
            f.write('\n'.join(result))
        f.writelines(results)
        pool.close()
        pool.join()

def yield_related(recipe_url):
    try:
        r = requests.get('http://'+base_domain+recipe_url)
    except:
        return

    soup = BeautifulSoup(r.text, 'html.parser')
    grid = soup.find('section', id='grid')
    if grid is None:
        return

    articles = grid.find_all('article')

    for article in articles:
        is_bad_card = ('product_card' in article['class'] or 
                      'marketing-card' in article['class'])
        if not is_bad_card:
            link = article.find('a')
            if link is not None and not link['href'].startswith('/video'):
                yield link['href']

def yield_recipes(filename):
    with open(filename, 'r') as f:
        for line in f:
            yield line.strip()

if __name__ == "__main__":
    main()