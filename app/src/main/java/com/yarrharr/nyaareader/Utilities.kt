package com.yarrharr.nyaareader

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.annotation.Keep
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.ArrayList

class Utilities private constructor(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("alternate_db", Context.MODE_PRIVATE)
    private val db = Firebase.firestore

    private val allSeries: ArrayList<Series>?
        get() {
            val gson = Gson()
            val type = object : TypeToken<ArrayList<Series?>?>() {}.type
            return gson.fromJson(sharedPreferences.getString(ListKeys.ALL.toString(), null), type)
        }
    private val finishedSeries: ArrayList<Series>?
        get() {
            val gson = Gson()
            val type = object : TypeToken<ArrayList<Series?>?>() {}.type
            return gson.fromJson(sharedPreferences.getString(ListKeys.FINISHED.toString(), null), type)
        }
    private val wantToReadSeries: ArrayList<Series>?
        get() {
            val gson = Gson()
            val type = object : TypeToken<ArrayList<Series?>?>() {}.type
            return gson.fromJson(sharedPreferences.getString(ListKeys.WANT_TO_READ.toString(), null), type)
        }
    private val readingSeries: ArrayList<Series>?
        get() {
            val gson = Gson()
            val type = object : TypeToken<ArrayList<Series?>?>() {}.type
            return gson.fromJson(sharedPreferences.getString(ListKeys.READING.toString(), null), type)
        }
    private val favoriteSeries: ArrayList<Series>?
        get() {
            val gson = Gson()
            val type = object : TypeToken<ArrayList<Series?>?>() {}.type
            return gson.fromJson(sharedPreferences.getString(ListKeys.FAVORITE.toString(), null), type)
        }

    @Keep
    enum class ListKeys {
        ALL,
        FINISHED,
        WANT_TO_READ,
        READING,
        FAVORITE
    }

    companion object {
        private var instance: Utilities? = null
        @Synchronized
        fun getInstance(context: Context): Utilities? {
            if (instance == null) {
                instance = Utilities(context)
            }
            return instance
        }

        private const val TAG = "Utilities"
    }

    init {
        initData()
        val editor = sharedPreferences.edit()
        val gson = Gson()
        if (finishedSeries == null) {
            editor.putString(ListKeys.FINISHED.toString(), gson.toJson(ArrayList<Series>()))
            editor.apply()
        }
        if (wantToReadSeries == null) {
            editor.putString(ListKeys.WANT_TO_READ.toString(), gson.toJson(ArrayList<Series>()))
            editor.commit()
        }
        if (readingSeries == null) {
            editor.putString(ListKeys.READING.toString(), gson.toJson(ArrayList<Series>()))
            editor.commit()
        }
        if (favoriteSeries == null) {
            editor.putString(ListKeys.FAVORITE.toString(), gson.toJson(ArrayList<Series>()))
            editor.commit()
        }
    }

    private fun initData() {
        db.collection("series")
                .get()
                .addOnSuccessListener { result ->
                    val seriesList = ArrayList<Series>()
                    for ((i, document) in result.withIndex()) {
                        Log.d(TAG, "${document.id} => ${document.data}")
                        val name = document.id
                        val imageUrl = document.data["imageUrl"] as String
                        var synopsis = document.data["synopsis"] as String?
                        if (synopsis == null) {
                            synopsis = "No synopsis has been added yet."
                        }
                        val series = Series(i, name, imageUrl, synopsis)
                        seriesList.add(series)
                    }

                    val editor = sharedPreferences.edit()
                    val gson = Gson()
                    editor.putString(ListKeys.ALL.toString(), gson.toJson(seriesList))
                    editor.apply()
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents.", exception)
                }
    }

    fun getSeriesById(id: Int): Series? {
        for (series in allSeries!!) {
            if (series.id == id) {
                return series
            }
        }
        return null
    }

    fun addSeriesToList(series: Series, key: ListKeys): Boolean {
        val seriesList = getList(key) ?: return false

        if (seriesList.add(series)) {
            val gson = Gson()
            val editor = sharedPreferences.edit()
            editor.remove(key.toString())
            editor.putString(key.toString(), gson.toJson(seriesList))
            editor.apply()
            return true
        }
        return false
    }

    fun removeSeriesFromList(series: Series, key: ListKeys): Boolean {
        val seriesList = getList(key) ?: return false

        if (seriesList.remove(series)) {
            val gson = Gson()
            val editor = sharedPreferences.edit()
            editor.remove(key.toString())
            editor.putString(key.toString(), gson.toJson(seriesList))
            editor.apply()
            return true
        }
        return false
    }

    fun getList(key: ListKeys): ArrayList<Series>? {
        return when (key) {
            ListKeys.FINISHED -> finishedSeries
            ListKeys.WANT_TO_READ -> wantToReadSeries
            ListKeys.READING -> readingSeries
            ListKeys.FAVORITE -> favoriteSeries
            else -> allSeries
        }
    }
}