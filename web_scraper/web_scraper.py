# Goal: Extract all links to books from this list
import requests
from bs4 import BeautifulSoup

import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore

import time

# Use the application default credentials
cred = credentials.Certificate("nuclear_launch_codes.json")
firebase_admin.initialize_app(cred, {
  'projectId': "light-novel-reader",
})

db = firestore.client()


class Book:
    def __init__(self, name, author, image_url, description):
        self.name = name
        self.author = author
        self.image_url = image_url
        self.description = description
    

def scrape_page(index):
    website = "https://www.goodreads.com"
    list_url = website + "/shelf/show/light-novel?page=" + str(index)
    html = requests.get(list_url).text
    soup = BeautifulSoup(html, 'lxml')
    left_container = soup.find("div", {"class": "leftContainer"})
    books = left_container.findAll("div", {"class": "left"})
    print(list_url)
    for b in books:
        title = b.find("a", {"class": "bookTitle"})
        book_name = title.text

        book_url = website + title.get("href")
        book_result = requests.get(book_url)
        book_src = book_result.content
        book_soup = BeautifulSoup(book_src, "html5lib")

        topcol = book_soup.find(id="topcol")
        if not topcol:
            continue
        image_url = scrape_image_url(topcol)

        metacol = topcol.find(id="metacol")
        if not metacol:
            continue
        book_author = scrape_author(metacol)

        book_description = scrape_description(metacol)
        
        book = Book(book_name, book_author, image_url, book_description)
        print(book.name)

        # doc_ref = db.collection("books").document(book.name)
        # doc_ref.set({
        #     "author": book.author,
        #     "imageUrl": book.image_url,
        #     "description": book.description
        # })


def scrape_image_url(topcol):
    imagecol = topcol.find(id="imagecol")
    if not imagecol:
        return
    cover_image = imagecol.find(id="coverImage")
    if not cover_image:
        return
    return cover_image.get("src")


def scrape_author(metacol):
    book_authors = metacol.find(id="bookAuthors")
    if not book_authors:
        return
    book_authors_div = book_authors.find("div")
    if not book_authors_div:
        return
    book_authors_link = book_authors_div.find("a")
    if not book_authors_link:
        return
    return book_authors_link.text


def scrape_description(metacol):
    description_container = metacol.find(id="descriptionContainer")
    if not description_container:
        return
    description_container_2_electric_boogaloo = description_container.find(id="description")
    if not description_container_2_electric_boogaloo:
        return
    book_descriptions = description_container_2_electric_boogaloo.findAll("span")
    if len(book_descriptions) == 0:
        return
    return book_descriptions[len(book_descriptions) - 1].text

max_num_pages = 25
for index in range(2,max_num_pages):
    scrape_page(index)