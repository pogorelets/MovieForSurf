package ru.lostpolygons.movieapplication.model

import com.google.gson.annotations.SerializedName

data class MovieResponse <I> (
    val page: Int,
    @SerializedName("total_results")
    val totalResults: Int,
    @SerializedName("total_pages")
    val totalPages: Int,
    val results: List<I>
)