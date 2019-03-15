package ru.lostpolygons.movieapplication.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.lostpolygons.movieapplication.presentation.MainActivity
import ru.lostpolygons.movieapplication.presentation.MovieViewModule

@Module
interface ActivityBindingModule {

    @ContributesAndroidInjector(modules = arrayOf(MovieViewModule::class))
    fun bindMainActivity(): MainActivity

}