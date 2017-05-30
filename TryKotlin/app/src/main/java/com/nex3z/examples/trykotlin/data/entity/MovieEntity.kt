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
        val popularity: Double? = 0.toDouble(),
        val vote_count: Int? = null,
        val video: Boolean = false,
        val vote_average: Double? = null
) {
}