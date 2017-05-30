package com.nex3z.examples.trykotlin.data.entity

class DiscoveryMovieEntity(
        var page: Int = 0,
        var results: List<MovieEntity>? = null,
        var total_results: Int = 0,
        var total_pages: Int = 0
) {
}