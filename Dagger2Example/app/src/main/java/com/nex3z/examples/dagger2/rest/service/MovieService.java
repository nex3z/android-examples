package com.nex3z.examples.dagger2.rest.service;

import com.nex3z.examples.dagger2.rest.model.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieService {

    String SORT_BY_POPULARITY_DESC = "popularity.desc";
    String SORT_BY_VOTE_AVERAGE_DESC = "vote_average.desc";
    String SORT_BY_VOTE_COUNT_DESC = "vote_count.desc";

    @GET("/3/discover/movie")
    Call<MovieResponse> getMovies(@Query("sort_by") String sortBy);

    @GET("/3/discover/movie")
    Call<MovieResponse> getMovies(@Query("sort_by") String sortBy, @Query("page") int page);
}
