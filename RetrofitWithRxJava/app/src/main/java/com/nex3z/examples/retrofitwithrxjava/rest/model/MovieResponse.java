package com.nex3z.examples.retrofitwithrxjava.rest.model;

import com.google.gson.annotations.SerializedName;
import com.nex3z.examples.retrofitwithrxjava.model.Movie;

import java.util.List;

public class MovieResponse {
    private long page;

    @SerializedName("results")
    private List<Movie> movies;

    public List<Movie> getMovies() {
        return movies;
    }
}
