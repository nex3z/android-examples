package com.nex3z.examples.trykotlin.data.entity

data class MovieEntity(
        val poster_path: String? = null,
        val adult: Boolean = false,
        val overview: String? = null,
        val release_date: String? = null,
        val genre_ids: List<Int>? = null,
        val id: String? = null,
        val original_title: String? = null,
        val original_language: String? = null,
        val title: String? = null,
        val backdrop_path: String? = null,
        val popularity: Double = 0.0,
        val vote_count: Int = 0,
        val video: Boolean = false,
        val vote_average: Double = 0.0
) {

    fun getPosterImageUrl(size: PosterSize): String =
            BASE_URL + size.value + "/" + poster_path

    enum class PosterSize(val value: String) {
        W92("w92"),
        W154("w154"),
        W185("w185"),
        W342("w342"),
        W500("w500"),
        W780("w780"),
        Original("original")
    }

    companion object {
        val BASE_URL = "http://image.tmdb.org/t/p/"
    }

}
