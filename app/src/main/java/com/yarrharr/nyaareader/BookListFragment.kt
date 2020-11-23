package com.yarrharr.nyaareader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_book_list.*

class BookListFragment : Fragment() {
    private val args: BookListFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        return inflater.inflate(R.layout.fragment_book_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postponeEnterTransition()

        recyclerViewBooks.layoutManager = GridLayoutManager(view.context, 2)

        val adapter = view.context?.let { BookRecyclerViewAdapter(it, args.bookListKey) } ?: return
        recyclerViewBooks.adapter = adapter
        Utilities.getInstance(view.context)?.getBookList(args.bookListKey)?.let { adapter.books = it }
        adapter.bookSelectedListener = object : BookRecyclerViewAdapter.BookSelectedListener {
            override fun onBookSelected(book: Book, bookImageView: ImageView) {
                val action = BookListFragmentDirections.actionBookListFragmentToBookFragment(book.id)
                val extras = FragmentNavigatorExtras(bookImageView to book.imageUrl)
                findNavController().navigate(action, extras)
            }
        }

        (view.parent as? ViewGroup)?.doOnPreDraw {
            startPostponedEnterTransition()
        }
    }
}