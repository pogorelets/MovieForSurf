package ru.lostpolygons.movieapplication.domain

import io.reactivex.Single
import ru.lostpolygons.movieapplication.model.ItemMovie
import ru.lostpolygons.movieapplication.model.MovieResponse
import javax.inject.Inject

class FilterMovie @Inject constructor(private val movieDataSource: MovieDataSource): SingleUseCase<MovieResponse<ItemMovie>, FilterMovie.Params>() {
    override fun execute(params: Params): Single<MovieResponse<ItemMovie>> {
        return movieDataSource.filterMovie(params.page,params.query)
    }
    class Params(val  page: Int, val query: String)
}