package com.nex3z.examples.recyclerview2.rest.service;

import android.support.annotation.StringDef;

import com.nex3z.examples.recyclerview2.rest.model.MovieResponse;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

public interface MovieService {

    String SORT_BY_POPULARITY_DESC = "popularity.desc";
    String SORT_BY_VOTE_AVERAGE_DESC = "vote_average.desc";
    String SORT_BY_VOTE_COUNT_DESC = "vote_count.desc";

    @StringDef({SORT_BY_POPULARITY_DESC, SORT_BY_VOTE_AVERAGE_DESC, SORT_BY_VOTE_COUNT_DESC})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SortType {}

    @GET("/3/discover/movie")
    Call<MovieResponse> getMovies(@Query("sort_by") @SortType String sortBy);

    @GET("/3/discover/movie")
    Call<MovieResponse> getMovies(@Query("sort_by") @SortType String sortBy, @Query("page") int page);
}
