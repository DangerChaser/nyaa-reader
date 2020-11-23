package com.yarrharr.nyaareader

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_series.*

class SeriesFragment() : Fragment() {
    private val args: SeriesFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(requireContext()).inflateTransition(R.transition.shared_image)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_series, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val seriesId = args.id
        val series = Utilities.getInstance(view.context)?.getSeriesById(seriesId) ?: return
        setSeries(series)
        handleList(series, Utilities.ListKeys.FINISHED)
        handleList(series, Utilities.ListKeys.WANT_TO_READ)
        handleList(series, Utilities.ListKeys.READING)
        handleList(series, Utilities.ListKeys.FAVORITE)
    }

    private fun setSeries(series: Series) {
        textViewSeriesName.text = series.name
        textViewSynopsis.text = series.synopsis
        seriesImageView.apply {
            transitionName = series.imageUrl
            Glide.with(this).asBitmap().load(series.imageUrl).into(this)
        }
    }

    private fun handleList(series: Series, key: Utilities.ListKeys) {
        val utilities: Utilities = view?.context?.let { Utilities.getInstance(it) } ?: return
        val seriesList: ArrayList<Series> = utilities.getList(key) ?: return

        var existsInList = false
        for (s in seriesList) {
            if (s.id == series.id) {
                existsInList = true
            }
        }

        val button: Button = when(key) {
            Utilities.ListKeys.READING -> buttonCurrentlyReading
            Utilities.ListKeys.WANT_TO_READ -> buttonWantToRead
            Utilities.ListKeys.FAVORITE -> buttonAddToFavorites
            Utilities.ListKeys.FINISHED -> buttonFinished
            else -> return
        }

        if (existsInList) {
            button.isEnabled = false
        }
        else {
            button.setOnClickListener {
                if (utilities.addSeriesToList(series, key)) {
                    Toast.makeText(view?.context, "$(series.name) Added", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(view?.context, "Something happened, try again", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}