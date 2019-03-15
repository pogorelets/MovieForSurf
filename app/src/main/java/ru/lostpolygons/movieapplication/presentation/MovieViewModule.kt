package ru.lostpolygons.movieapplication.presentation


import android.app.Activity
import dagger.Binds
import dagger.Module
import ru.lostpolygons.movieapplication.di.scope.ActivityScope


@Module
abstract class MovieViewModule(private val activity: Activity) {

    @Binds
    abstract fun view(view: MainActivity): MovieContract.View
}