package com.nex3z.examlpes.mvp.data.rest.model;

import com.google.gson.annotations.SerializedName;
import com.nex3z.examlpes.mvp.data.entity.MovieEntity;

import java.util.List;

public class MovieResponse {
    private long page;

    @SerializedName("results")
    private List<MovieEntity> movies;

    public List<MovieEntity> getMovies() {
        return movies;
    }
}
