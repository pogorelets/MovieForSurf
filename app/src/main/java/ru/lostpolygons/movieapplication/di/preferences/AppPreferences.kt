package ru.apptimizm.formatfit.di.preferences

import android.content.Context
import android.preference.PreferenceManager
import com.google.gson.Gson

import javax.inject.Inject

class AppPreferences @Inject constructor(override val context: Context) : PreferenceProvider {
    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
    private val editor = sharedPreferences.edit()
    private val FAVORITE_LIST = "FAVORITE_LIST"
    private val gson = Gson()

    override fun saveFavorite(id: Int) {
        val list = getFavorite()
        list.add(id)
        saveFavorite(list)
    }

    override fun removeFavorite(id: Int) {
        val list = getFavorite()
        list.remove(id)
        saveFavorite(list)
    }

    override fun getFavorite(): MutableList<Int> {
        val list = getString(FAVORITE_LIST, "")
        return if (list == "") mutableListOf()
        else gson.fromJson(list, Array<Int>::class.java).toMutableList()
    }

    private fun saveFavorite (list: List<Int>){ editor.putString(FAVORITE_LIST,gson.toJson(list)).apply() }

    private fun getString(key: String, default: String) = sharedPreferences.getString(key, default)
}