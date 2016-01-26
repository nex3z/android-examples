package com.nex3z.examples.dagger2.data.rest.model;

import com.google.gson.annotations.SerializedName;
import com.nex3z.examples.dagger2.data.entity.MovieEntity;

import java.util.List;

public class MovieResponse {
    private long page;

    @SerializedName("results")
    private List<MovieEntity> movies;

    public List<MovieEntity> getMovies() {
        return movies;
    }
}
