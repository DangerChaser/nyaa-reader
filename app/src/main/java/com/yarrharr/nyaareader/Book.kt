package com.yarrharr.nyaareader

class Book(var id: Int, var name: String, var author: String,var imageUrl: String, var longDescription: String) {
    var isExpanded = false
    override fun toString(): String {
        return "Book(id=$id, name='$name', author='$author', imageUrl='$imageUrl', longDescription='$longDescription')"
    }

}