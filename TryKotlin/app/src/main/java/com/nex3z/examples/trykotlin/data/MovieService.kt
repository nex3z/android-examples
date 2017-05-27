package com.nex3z.examples.trykotlin.data

import com.nex3z.examples.trykotlin.data.entity.DiscoveryMovieEntity
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {

    @GET("/3/discover/movie")
    fun discoverMovies(@Query("page") page: Int, @Query("sort_by") sortBy: String)
            : Call<DiscoveryMovieEntity>

}