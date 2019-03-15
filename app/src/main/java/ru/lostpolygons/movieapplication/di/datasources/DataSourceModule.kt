package ru.lostpolygons.movieapplication.di.datasources

import dagger.Module
import dagger.Provides
import ru.lostpolygons.movieapplication.data.MovieApi
import ru.lostpolygons.movieapplication.data.MovieRepository
import ru.lostpolygons.movieapplication.di.network.Network
import ru.lostpolygons.movieapplication.domain.MovieDataSource
import javax.inject.Singleton

@Module
class DataSourceModule {
    @Provides
    @Singleton
    fun provideMovieDataSource(
        network: Network,
        movieApi: MovieApi
    ): MovieDataSource = MovieRepository(network, movieApi)

}