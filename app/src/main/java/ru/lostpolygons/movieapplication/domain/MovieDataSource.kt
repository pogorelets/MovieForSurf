package ru.lostpolygons.movieapplication.domain

import io.reactivex.Single
import ru.lostpolygons.movieapplication.model.ItemMovie
import ru.lostpolygons.movieapplication.model.MovieResponse

interface MovieDataSource {

    fun getMovies(page: Int): Single<MovieResponse<ItemMovie>>

    fun filterMovie(page: Int, query: String  ): Single<MovieResponse<ItemMovie>>
}