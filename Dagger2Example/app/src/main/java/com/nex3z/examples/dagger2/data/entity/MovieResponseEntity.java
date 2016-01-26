package com.nex3z.examples.dagger2.data.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieResponseEntity {
    private long page;

    @SerializedName("results")
    private List<MovieEntity> movies;

    public List<MovieEntity> getMovies() {
        return movies;
    }
}
