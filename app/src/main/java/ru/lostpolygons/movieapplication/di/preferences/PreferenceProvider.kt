package ru.apptimizm.formatfit.di.preferences
import android.content.Context

interface PreferenceProvider {
    val context: Context
    fun saveFavorite(id: Int)
    fun removeFavorite(id: Int)
    fun getFavorite(): MutableList<Int>


}