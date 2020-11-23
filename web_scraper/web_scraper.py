# Goal: Extract all links to books from this list
import requests
from bs4 import BeautifulSoup

import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore

# Use the application default credentials
cred = credentials.Certificate("nuclear_launch_codes.json")
firebase_admin.initialize_app(cred, {
  'projectId': "light-novel-reader",
})

db = firestore.client()


class Series:
    def __init__(self, _id, name, image_url, synopsis):
        self.id = _id
        self.name = name
        self.image_url = image_url
        self.synopsis = synopsis
    

website = "https://www.anime-planet.com"

def main():
    domain = "/manga/tags/light-novels"
    first_page = requests.get(website + domain).text
    first_page_soup = BeautifulSoup(first_page, "lxml")

    collection = first_page_soup.find("ul", {"class" : "cardDeck cardGrid"})
    scrape_collection(collection)

    pagination = first_page_soup.find("div", {"class" : "pagination aligncenter"})
    next_page = pagination.find("li", {"class" : "next"})
    next_page_a = next_page.find("a")

    index = 2
    while (next_page_a):
        print(index)

        query = next_page_a.get("href")
        html = requests.get(website + domain + query).text
        soup = BeautifulSoup(html, 'lxml')
        collection = soup.find("ul", {"class" : "cardDeck cardGrid"})
        scrape_collection(collection)

        # Get next page
        pagination = soup.find("div", {"class" : "pagination aligncenter"})
        next_page = pagination.find("li", {"class" : "next"})
        next_page_a = next_page.find("a")
        index += 1


def scrape_collection(collection):
    cards = collection.findAll("li", {"class" : "card"})
    
    for card in cards:
        series = scrape_series(card)
        add_to_db(series)
        print(series.name)


def scrape_series(card):
    href = card.find("a").get("href")
    _id = href.removeprefix("/manga/")

    result = requests.get(website + href)
    item_src = result.content
    item_soup = BeautifulSoup(item_src, "lxml")

    name = item_soup.find("h1", {"itemprop": "name"}).getText()

    entry = item_soup.find("section", {"id" : "entry"})
    image_url = website + entry.find("img").get("src")
    snyopsis = entry.find("p").getText()
    
    return Series(_id, name, image_url, snyopsis)


def add_to_db(series):
    doc_ref = db.collection("series").document(series.id)
    doc_ref.set({
        "name": series.name,
        "imageUrl": series.image_url,
        "synopsis": series.synopsis
    })



main()