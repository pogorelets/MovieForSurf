package ru.lostpolygons.movieapplication.di.application

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class ApplicationModule(private val appContext: Context) {
    @Provides
    @Singleton
    fun provideApplication(): Application = appContext as Application

    @Provides
    @Singleton
    fun provideContext(): Context = appContext

}