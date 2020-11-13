package com.yarrharr.nyaareader

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.ArrayList

const val BOOK_LIST_KEY = "BOOK_LIST_KEY"
class Utilities private constructor(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("alternate_db", Context.MODE_PRIVATE)

    enum class BookListKeys(s: String) {
        ALL("ALL_BOOKS"),
        FINISHED("FINISHED_BOOKS"),
        WISHLISTED("WISHLISTED_BOOKS"),
        IN_PROGRESS("IN_PROGRESS_BOOKS"),
        FAVORITE("FAVORITE_BOOKS")
    }

    private fun initData() {
        val books = ArrayList<Book>()
        val editor = sharedPreferences.edit()
        val gson = Gson()
        editor.putString(BookListKeys.ALL.toString(), gson.toJson(books))
        editor.commit()
    }

    private val allBooks: ArrayList<Book>?
        get() {
            val gson = Gson()
            val type = object : TypeToken<ArrayList<Book?>?>() {}.type
            return gson.fromJson(sharedPreferences.getString(BookListKeys.ALL.toString(), null), type)
        }
    private val finishedBooks: ArrayList<Book>?
        get() {
            val gson = Gson()
            val type = object : TypeToken<ArrayList<Book?>?>() {}.type
            return gson.fromJson(sharedPreferences.getString(BookListKeys.FINISHED.toString(), null), type)
        }
    private val wishlistedBooks: ArrayList<Book>?
        get() {
            val gson = Gson()
            val type = object : TypeToken<ArrayList<Book?>?>() {}.type
            return gson.fromJson(sharedPreferences.getString(BookListKeys.WISHLISTED.toString(), null), type)
        }
    private val currentlyReadingBooks: ArrayList<Book>?
        get() {
            val gson = Gson()
            val type = object : TypeToken<ArrayList<Book?>?>() {}.type
            return gson.fromJson(sharedPreferences.getString(BookListKeys.IN_PROGRESS.toString(), null), type)
        }
    private val favoriteBooks: ArrayList<Book>?
        get() {
            val gson = Gson()
            val type = object : TypeToken<ArrayList<Book?>?>() {}.type
            return gson.fromJson(sharedPreferences.getString(BookListKeys.FAVORITE.toString(), null), type)
        }

    fun getBookById(id: Int): Book? {
        for (book in allBooks!!) {
            if (book.id == id) {
                return book
            }
        }
        return null
    }

    fun addBookToList(book: Book, key: BookListKeys): Boolean {
        val books = when (key) {
            BookListKeys.FINISHED -> finishedBooks
            BookListKeys.WISHLISTED -> wishlistedBooks
            BookListKeys.IN_PROGRESS -> currentlyReadingBooks
            BookListKeys.FAVORITE -> favoriteBooks
            else -> allBooks
        } ?: return false

        if (books.add(book)) {
            val gson = Gson()
            val editor = sharedPreferences.edit()
            editor.remove(key.toString())
            editor.putString(key.toString(), gson.toJson(books))
            editor.commit()
            return true
        }
        return false
    }

    fun removeBookFromList(book: Book, key: BookListKeys): Boolean {
        val books = when (key) {
            BookListKeys.FINISHED -> finishedBooks
            BookListKeys.WISHLISTED -> wishlistedBooks
            BookListKeys.IN_PROGRESS -> currentlyReadingBooks
            BookListKeys.FAVORITE -> favoriteBooks
            else -> allBooks
        } ?: return false

        if (books.remove(book)) {
            val gson = Gson()
            val editor = sharedPreferences.edit()
            editor.remove(key.toString())
            editor.putString(key.toString(), gson.toJson(books))
            editor.commit()
            return true
        }
        return false
    }

    fun getBookList(key: BookListKeys): ArrayList<Book>? {
        return when (key) {
            BookListKeys.ALL -> allBooks
            BookListKeys.FAVORITE -> favoriteBooks
            BookListKeys.IN_PROGRESS -> currentlyReadingBooks
            BookListKeys.WISHLISTED -> wishlistedBooks
            BookListKeys.FINISHED -> finishedBooks
        }
    }

    companion object {
        private var instance: Utilities? = null
        @Synchronized
        fun getInstance(context: Context): Utilities? {
            if (instance == null) {
                instance = Utilities(context)
            }
            return instance
        }
    }

    init {
        if (allBooks == null) {
            initData()
        }
        val editor = sharedPreferences.edit()
        val gson = Gson()
        if (finishedBooks == null) {
            editor.putString(BookListKeys.FINISHED.toString(), gson.toJson(ArrayList<Book>()))
            editor.commit()
        }
        if (wishlistedBooks == null) {
            editor.putString(BookListKeys.WISHLISTED.toString(), gson.toJson(ArrayList<Book>()))
            editor.commit()
        }
        if (currentlyReadingBooks == null) {
            editor.putString(BookListKeys.IN_PROGRESS.toString(), gson.toJson(ArrayList<Book>()))
            editor.commit()
        }
        if (favoriteBooks == null) {
            editor.putString(BookListKeys.FAVORITE.toString(), gson.toJson(ArrayList<Book>()))
            editor.commit()
        }
    }
}