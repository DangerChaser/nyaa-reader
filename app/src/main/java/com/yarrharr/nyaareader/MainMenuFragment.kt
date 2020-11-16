package com.yarrharr.nyaareader

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.NavAction
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_main_menu.*

class MainMenuFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonAllBooks.setOnClickListener {
            val action = MainMenuFragmentDirections.mainMenuFragmentToBookListFragment(Utilities.BookListKeys.ALL)
            Navigation.findNavController(view).navigate(action)
        }
        buttonAlreadyRead.setOnClickListener {
            val action = MainMenuFragmentDirections.mainMenuFragmentToBookListFragment(Utilities.BookListKeys.FINISHED)
            Navigation.findNavController(view).navigate(action)
        }
        buttonCurrentlyReading.setOnClickListener {
            val action = MainMenuFragmentDirections.mainMenuFragmentToBookListFragment(Utilities.BookListKeys.IN_PROGRESS)
            Navigation.findNavController(view).navigate(action)
        }
        buttonFavorites.setOnClickListener {
            val action = MainMenuFragmentDirections.mainMenuFragmentToBookListFragment(Utilities.BookListKeys.FAVORITE)
            Navigation.findNavController(view).navigate(action)
        }
        buttonWantToRead.setOnClickListener {
            val action = MainMenuFragmentDirections.mainMenuFragmentToBookListFragment(Utilities.BookListKeys.WISHLISTED)
            Navigation.findNavController(view).navigate(action)
        }
    }
}