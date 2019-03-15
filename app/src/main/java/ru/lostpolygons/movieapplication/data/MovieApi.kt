package ru.lostpolygons.movieapplication.data

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import ru.lostpolygons.movieapplication.data.NetworkConstant.API_KEY
import ru.lostpolygons.movieapplication.data.NetworkConstant.LANGUAGE
import ru.lostpolygons.movieapplication.data.NetworkConstant.SORT_BY
import ru.lostpolygons.movieapplication.model.ItemMovie
import ru.lostpolygons.movieapplication.model.MovieResponse

object NetworkConstant{
    const val API_KEY = "6ccd72a2a8fc239b13f209408fc31c33"
    const val LANGUAGE = "ru-RU"
    const val SORT_BY = "popularity.desc"

}

interface MovieApi {

    @GET("discover/movie")
    fun getMovies(@Query("api_key") key: String = API_KEY,
                  @Query("language") language: String = LANGUAGE,
                  @Query("sort_by") sort: String = SORT_BY,
                  @Query("page") page: Int = 1): Single<MovieResponse<ItemMovie>>

    @GET("search/movie")
    fun filterMovie(@Query("api_key") key: String = API_KEY,
                    @Query("language") language: String = LANGUAGE,
                    @Query("page") page: Int = 1,
                    @Query("query") query: String  ): Single<MovieResponse<ItemMovie>>
}