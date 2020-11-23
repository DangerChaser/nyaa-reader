package com.yarrharr.nyaareader

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_book.view.*
import java.util.ArrayList

class BookRecyclerViewAdapter(private val context: Context, private val bookListKey: Utilities.BookListKeys) : RecyclerView.Adapter<BookRecyclerViewAdapter.ViewHolder>() {
    companion object { private const val TAG = "BookRecyclerViewAdapter" }

    var books = ArrayList<Book>()
        set(books) {
            field = books
            notifyDataSetChanged()
        }

    interface BookSelectedListener {
        fun onBookSelected(book: Book, bookImageView: ImageView)
    }
    lateinit var bookSelectedListener: BookSelectedListener
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_book, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: Called")
        val book = books[position]
        holder.bindBook(book)
    }

    override fun getItemCount(): Int {
        return books.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val parent: CardView = itemView.findViewById(R.id.parent)
        private val bookImageView: ImageView = itemView.findViewById(R.id.bookImageView)
        private val textName: TextView = itemView.findViewById(R.id.textName)
        private val downArrow: ImageView = itemView.findViewById(R.id.buttonDownArrow)
        private val upArrow: ImageView = itemView.findViewById(R.id.buttonUpArrow)
        private val expandedRelativeLayout: RelativeLayout = itemView.findViewById(R.id.expandedRelativeLayout)
        private val textAuthorName: TextView = itemView.findViewById(R.id.textAuthorName)
        private val textShortDescription: TextView = itemView.findViewById(R.id.textShortDescription)
        private val buttonDelete: TextView = itemView.findViewById(R.id.buttonDelete)

        init {
            downArrow.setOnClickListener {
                val book = books[adapterPosition]
                book.isExpanded = true
                notifyItemChanged(adapterPosition)
            }
            upArrow.setOnClickListener {
                val book = books[adapterPosition]
                book.isExpanded = false
                notifyItemChanged(adapterPosition)
            }
        }

        fun bindBook(book: Book) {
            textName.text = book.name
            textAuthorName.text = book.author
            bookImageView.apply {
                transitionName = book.imageUrl
                Glide.with(context).asBitmap().load(book.imageUrl).into(this)
            }

            bookImageView.setOnClickListener {
                bookSelectedListener.onBookSelected(book, bookImageView)
            }

            if (book.isExpanded) {
                bindExpandedView(book)
            }
            else {
                TransitionManager.beginDelayedTransition(parent)
                expandedRelativeLayout.visibility = View.GONE
                downArrow.visibility = View.VISIBLE
            }
        }

        private fun bindExpandedView(book: Book) {
            TransitionManager.beginDelayedTransition(parent)
            expandedRelativeLayout.visibility = View.VISIBLE
            downArrow.visibility = View.GONE
            if (bookListKey == Utilities.BookListKeys.ALL) {
                buttonDelete.visibility = View.GONE
                return
            }

            buttonDelete.visibility = View.VISIBLE
            buttonDelete.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                builder.setMessage("Are you sure you want to remove " + book.name + "?")
                builder.setPositiveButton("Yes") { _, _ ->
                    val utilities = Utilities.getInstance(context) ?: return@setPositiveButton
                    if (utilities.removeBookFromList(book, bookListKey)) {
                        Toast.makeText(context, book.name + " removed", Toast.LENGTH_SHORT).show()
                        notifyDataSetChanged()
                    } else {
                        Toast.makeText(context, "Something wrong happened, please try again", Toast.LENGTH_SHORT).show()
                    }
                }
                builder.setNegativeButton("No") { _, _ -> }
                builder.create().show()
            }
        }
    }
}