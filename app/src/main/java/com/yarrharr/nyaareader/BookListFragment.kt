package com.yarrharr.nyaareader

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_book_list.*

class BookListFragment : Fragment() {
    private val args: BookListFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_book_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerViewBooks.layoutManager = GridLayoutManager(view.context, 2)
        val adapter = view.context?.let { BookRecyclerViewAdapter(it, args.bookListKey) }
        recyclerViewBooks.adapter = adapter

        val utilities = Utilities.getInstance(view.context)
        if (utilities != null) {
            utilities.getBookList(args.bookListKey)?.let { adapter?.setBooks(it) }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(key: Utilities.BookListKeys) =
            BookListFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(BOOK_LIST_KEY, key)
                }
            }
    }
}