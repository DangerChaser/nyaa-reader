package com.yarrharr.nyaareader

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonAllBooks.setOnClickListener {
            val intent = Intent(this@MainActivity, BookListActivity::class.java)
            intent.putExtra(BOOK_LIST_KEY, Utilities.BookListKeys.ALL)
            startActivity(intent)
        }
        buttonAlreadyRead.setOnClickListener {
            val intent = Intent(this@MainActivity, BookListActivity::class.java)
            intent.putExtra(BOOK_LIST_KEY, Utilities.BookListKeys.FINISHED)
            startActivity(intent)
        }
        buttonCurrentlyReading.setOnClickListener {
            val intent = Intent(this@MainActivity, BookListActivity::class.java)
            intent.putExtra(BOOK_LIST_KEY, Utilities.BookListKeys.IN_PROGRESS)
            startActivity(intent)
        }
        buttonFavorites.setOnClickListener {
            val intent = Intent(this@MainActivity, BookListActivity::class.java)
            intent.putExtra(BOOK_LIST_KEY, Utilities.BookListKeys.FAVORITE)
            startActivity(intent)
        }
        buttonWantToRead.setOnClickListener {
            val intent = Intent(this@MainActivity, BookListActivity::class.java)
            intent.putExtra(BOOK_LIST_KEY, Utilities.BookListKeys.WISHLISTED)
            startActivity(intent)
        }

        Utilities.Companion.getInstance(this) // Calls constructor and initializes Utilities
    }
}