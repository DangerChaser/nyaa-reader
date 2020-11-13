package com.yarrharr.nyaareader

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_book_list.*

class BookListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_list)

        val utilities = Utilities.Companion.getInstance(this) ?: return

        val intent = intent ?: return
        val key = when (intent.getStringExtra(BOOK_LIST_KEY)) {
            Utilities.BookListKeys.ALL.toString() -> Utilities.BookListKeys.ALL
            Utilities.BookListKeys.FAVORITE.toString() -> Utilities.BookListKeys.FAVORITE
            Utilities.BookListKeys.FINISHED.toString() -> Utilities.BookListKeys.FINISHED
            Utilities.BookListKeys.IN_PROGRESS.toString() -> Utilities.BookListKeys.IN_PROGRESS
            Utilities.BookListKeys.WISHLISTED.toString() -> Utilities.BookListKeys.WISHLISTED
            else -> null
        }
            ?: return

        recyclerViewBooks.layoutManager = LinearLayoutManager(this)
        val adapter = BookRecyclerViewAdapter(this, key)
        recyclerViewBooks.adapter = adapter
        utilities.getBookList(key)?.let { adapter.setBooks(it) }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}