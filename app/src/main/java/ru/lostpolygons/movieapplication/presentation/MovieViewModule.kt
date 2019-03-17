package ru.lostpolygons.movieapplication.presentation

import android.app.Activity
import dagger.Binds
import dagger.Module


@Module
abstract class MovieViewModule(private val activity: Activity) {

    @Binds
    abstract fun view(view: MainActivity): MovieContract.View
}