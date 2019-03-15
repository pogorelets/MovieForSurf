package ru.lostpolygons.movieapplication.data

import io.reactivex.Single
import ru.lostpolygons.movieapplication.di.network.Network
import ru.lostpolygons.movieapplication.domain.MovieDataSource
import ru.lostpolygons.movieapplication.model.ItemMovie
import ru.lostpolygons.movieapplication.model.MovieResponse
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val network: Network,
    private val movieApi: MovieApi
): MovieDataSource {
    override fun getMovies(page: Int): Single<MovieResponse<ItemMovie>> {
        val request = movieApi.getMovies(page = page)
        return network.prepareRequest(request)
    }

    override fun filterMovie(page: Int, query: String): Single<MovieResponse<ItemMovie>> {
        val request = movieApi.filterMovie(page = page, query = query)
        return network.prepareRequest(request)
    }
}