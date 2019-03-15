package ru.lostpolygons.movieapplication.di.application

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import ru.lostpolygons.movieapplication.di.datasources.DataSourceModule
import ru.lostpolygons.movieapplication.di.module.ActivityBindingModule
import ru.lostpolygons.movieapplication.di.network.NetworkModule
import ru.lostpolygons.movieapplication.presentation.MovieModule
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        ApplicationModule::class,
        AndroidSupportInjectionModule::class,
        ActivityBindingModule::class,
        MovieModule::class,
        NetworkModule::class,
        DataSourceModule::class
    ]
)

interface ApplicationComponent : AndroidInjector<AppLoader> {
    //fun resourceProvider(): ResourceProvider
   // fun preferenceProvider(): PreferenceProvider
}
