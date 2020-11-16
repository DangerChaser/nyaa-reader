package com.yarrharr.nyaareader

import android.content.Context
import android.content.Intent
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
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.bumptech.glide.Glide
import java.util.ArrayList

class BookRecyclerViewAdapter(private val context: Context, private val bookListKey: Utilities.BookListKeys) : RecyclerView.Adapter<BookRecyclerViewAdapter.ViewHolder>() {
    private var books = ArrayList<Book>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_book, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: Called")

        val utilities = Utilities.getInstance(context) ?: return

        val book = books[position]

        holder.textName.text = book.name
        holder.textAuthorName.text = book.author
        Glide.with(context).asBitmap().load(book.imageUrl).into(holder.imageBook)
        holder.parent.setOnClickListener {
            val intent = Intent(context, BookActivity::class.java)
            intent.putExtra(BookActivity.BOOK_ID_KEY, book.id)
            context.startActivity(intent)
        }


        if (!book.isExpanded) {
            TransitionManager.beginDelayedTransition(holder.parent)
            holder.expandedRelativeLayout.visibility = View.GONE
            holder.downArrow.visibility = View.VISIBLE
            return
        }

        TransitionManager.beginDelayedTransition(holder.parent)
        holder.expandedRelativeLayout.visibility = View.VISIBLE
        holder.downArrow.visibility = View.GONE
        if (bookListKey == Utilities.BookListKeys.ALL) {
            holder.buttonDelete.visibility = View.GONE
            return
        }

        holder.buttonDelete.visibility = View.VISIBLE
        holder.buttonDelete.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setMessage("Are you sure you want to remove " + book.name + "?")
            builder.setPositiveButton("Yes") { _, _ ->
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

    override fun getItemCount(): Int {
        return books.size
    }

    fun setBooks(books: ArrayList<Book>) {
        this.books = books
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val parent: CardView = itemView.findViewById(R.id.parent)
        internal val imageBook: ImageView = itemView.findViewById(R.id.imageViewBook)
        internal val textName: TextView = itemView.findViewById(R.id.textName)
        internal val downArrow: ImageView = itemView.findViewById(R.id.buttonDownArrow)
        internal val upArrow: ImageView = itemView.findViewById(R.id.buttonUpArrow)
        internal val expandedRelativeLayout: RelativeLayout = itemView.findViewById(R.id.expandedRelativeLayout)
        internal val textAuthorName: TextView = itemView.findViewById(R.id.textAuthorName)
        internal val textShortDescription: TextView = itemView.findViewById(R.id.textShortDescription)
        internal val buttonDelete: TextView = itemView.findViewById(R.id.buttonDelete)

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
    }

    companion object {
        private const val TAG = "BookRecyclerViewAdapter"
    }
}