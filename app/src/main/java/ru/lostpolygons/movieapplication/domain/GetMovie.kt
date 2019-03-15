package ru.lostpolygons.movieapplication.domain


import io.reactivex.Single
import ru.lostpolygons.movieapplication.model.ItemMovie
import ru.lostpolygons.movieapplication.model.MovieResponse
import javax.inject.Inject

class GetMovie @Inject constructor(private val movieDataSource: MovieDataSource): SingleUseCase<MovieResponse<ItemMovie>, GetMovie.Params>() {
    override fun execute(params: Params): Single<MovieResponse<ItemMovie>> {
        return movieDataSource.getMovies(params.page)
    }
    class Params(val  page: Int)
}