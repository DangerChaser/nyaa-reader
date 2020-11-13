package com.yarrharr.nyaareader

class Book(var id: Int, var name: String, var author: String, var pages: Int, var imageUrl: String, var shortDescription: String, var longDescription: String) {
    var isExpanded = false
    override fun toString(): String {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", pages=" + pages +
                ", imageUrl='" + imageUrl + '\'' +
                ", shortDescription='" + shortDescription + '\'' +
                ", longDescription='" + longDescription + '\'' +
                '}'
    }
}