package com.yarrharr.nyaareader

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.util.ArrayList

class SeriesRecyclerViewAdapter(private val context: Context, private val listKey: Utilities.ListKeys) : RecyclerView.Adapter<SeriesRecyclerViewAdapter.ViewHolder>() {
    companion object { private const val TAG = "SeriesRecyclerViewAdapt" }

    var seriesList = ArrayList<Series>()
        set(seriesList) {
            field = seriesList
            notifyDataSetChanged()
        }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_series, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: Called")
        val series = seriesList[position]
        holder.bindSeries(series)
    }

    override fun getItemCount(): Int {
        return seriesList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val parent: CardView = itemView.findViewById(R.id.parent)
        private val seriesImageView: ImageView = itemView.findViewById(R.id.seriesImageView)
        private val textViewName: TextView = itemView.findViewById(R.id.textName)

        fun bindSeries(series: Series) {
            textViewName.text = series.name
            seriesImageView.apply {
                transitionName = series.imageUrl
                Glide.with(context).asBitmap().load(series.imageUrl).into(this)
                setOnClickListener {
                    val action = SeriesListFragmentDirections.actionBookListFragmentToBookFragment(series.id)
                    val extras = FragmentNavigatorExtras(this to series.imageUrl)
                    findNavController().navigate(action, extras)
                }
            }
        }
    }
}