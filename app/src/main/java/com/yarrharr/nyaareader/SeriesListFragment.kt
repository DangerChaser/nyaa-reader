package com.yarrharr.nyaareader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_series_list.*

class SeriesListFragment : Fragment() {
    private val args: SeriesListFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        return inflater.inflate(R.layout.fragment_series_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postponeEnterTransition()

        recyclerViewSeries.layoutManager = GridLayoutManager(view.context, 2)

        val adapter = view.context?.let { SeriesRecyclerViewAdapter(it, args.listKey) } ?: return
        recyclerViewSeries.adapter = adapter
        Utilities.getInstance(view.context)?.getList(args.listKey)?.let { adapter.seriesList = it }

        (view.parent as? ViewGroup)?.doOnPreDraw {
            startPostponedEnterTransition()
        }
    }
}