package com.yarrharr.nyaareader

class Series(var id: Int, var name: String, var imageUrl: String, var synopsis: String) {
    override fun toString(): String {
        return "Series(id=$id, name='$name', imageUrl='$imageUrl', synopsis='$synopsis')"
    }
}