""" Scrapes recipes from All-Recipes.
    Sorry in advance for DDOS'ing your site
"""

import requests
from bs4 import BeautifulSoup
from settings import start_urls, base_domain
from multiprocessing.dummy import Pool as ThreadPool


def main():
    url = start_urls[0]
    r = requests.get(url)

    html = r.text
    soup = BeautifulSoup(html, 'html.parser')

    pool = ThreadPool(10)
    results = pool.map(yield_from_category, start_urls)
    pool.close()
    pool.join()

    with open('recipe_urls.txt', 'w') as f:
        for result in results:
            f.write('\n'.join(result))
    print("Done!")


def yield_from_category(category_url):
    r = requests.get(category_url)
    soup = BeautifulSoup(r.text, 'html.parser')
    grid = soup.find('section', id='grid')
    articles = grid.find_all('article')

    for article in articles:
        is_bad_card = ('product_card' in article['class'] or 
                      'marketing-card' in article['class'])
        if not is_bad_card:
            link = article.find('a')
            if link is not None and not link['href'].startswith('/video'):
                yield link['href']
















if __name__ == "__main__":
    main() 