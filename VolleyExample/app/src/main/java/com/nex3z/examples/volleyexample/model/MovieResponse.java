package com.nex3z.examples.volleyexample.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieResponse {
    private long page;

    @SerializedName("results")
    private List<Movie> movies;

    public List<Movie> getMovies() {
        return movies;
    }
}
