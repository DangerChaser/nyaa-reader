package com.yarrharr.nyaareader

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_book.*

class BookFragment() : Fragment() {
    private val args: BookFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(requireContext()).inflateTransition(R.transition.shared_image)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_book, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bookId = args.bookId
        val book = Utilities.getInstance(view.context)?.getBookById(bookId) ?: return
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
        bookImageView.apply {
            transitionName = book.imageUrl
            Glide.with(this).asBitmap().load(book.imageUrl).into(this)
        }
    }

    private fun handleList(book: Book, key: Utilities.BookListKeys) {
        val utilities: Utilities = view?.context?.let { Utilities.getInstance(it) } ?: return
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
                    Toast.makeText(view?.context, "$(book.name) Added", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(view?.context, "Something happened, try again", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}