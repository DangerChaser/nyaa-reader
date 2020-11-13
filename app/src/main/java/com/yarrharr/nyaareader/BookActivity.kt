package com.yarrharr.nyaareader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_book.*

class BookActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book)
        val intent = intent ?: return

        val bookId = intent.getIntExtra(BOOK_ID_KEY, -1)
        if (bookId == -1) {
            return
        }

        val book = Utilities.getInstance(this)?.getBookById(bookId) ?: return
        setBook(book)
        handleList(book, Utilities.BookListKeys.FINISHED)
        handleList(book, Utilities.BookListKeys.WISHLISTED)
        handleList(book, Utilities.BookListKeys.IN_PROGRESS)
        handleList(book, Utilities.BookListKeys.FAVORITE)
    }

    private fun setBook(book: Book) {
        textViewBookName.text = book.name
        textViewAuthorName.text = book.author
        textViewLongDescription.text = book.longDescription
        Glide.with(this).asBitmap().load(book.imageUrl).into(imageViewBook)
    }

    private fun handleList(book: Book, key: Utilities.BookListKeys) {
        val utilities: Utilities = Utilities.getInstance(this) ?: return
        val books: ArrayList<Book> = utilities.getBookList(key) ?: return

        var existsInList = false
        for (b in books) {
            if (b.id == book.id) {
                existsInList = true
            }
        }

        val button: Button = when(key) {
            Utilities.BookListKeys.IN_PROGRESS -> buttonCurrentlyReading
            Utilities.BookListKeys.WISHLISTED -> buttonWishlist
            Utilities.BookListKeys.FAVORITE -> buttonAddToFavorites
            Utilities.BookListKeys.FINISHED -> buttonFinished
            else -> return
        }

        if (existsInList) {
            button.isEnabled = false
        }
        else {
            button.setOnClickListener {
                if (utilities.addBookToList(book, key)) {
                    Toast.makeText(this@BookActivity, "Book Added", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@BookActivity, "Something happened, try again", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        const val BOOK_ID_KEY = "bookId"
    }
}