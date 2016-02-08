package com.nex3z.examlpes.mvp.presentation.mapper;


import com.nex3z.examlpes.mvp.domain.Movie;
import com.nex3z.examlpes.mvp.presentation.internal.PerActivity;
import com.nex3z.examlpes.mvp.presentation.model.MovieModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.inject.Inject;

@PerActivity
public class MovieModelDataMapper {

    @Inject
    public MovieModelDataMapper() { }

    public MovieModel transform(Movie movie) {
        if (movie == null) {
            throw new IllegalArgumentException("Cannot transform a null value");
        }

        MovieModel movieModel = new MovieModel();
        movieModel.setAdult(movie.isAdult());
        movieModel.setBackdropPath(movie.getBackdropPath());
        movieModel.setGenreIds(movie.getGenreIds());
        movieModel.setId(movie.getId());
        movieModel.setOriginalLanguage(movie.getOriginalLanguage());
        movieModel.setOriginalTitle(movie.getOriginalTitle());
        movieModel.setOverview(movie.getOverview());
        movieModel.setPopularity(movie.getPopularity());
        movieModel.setPosterPath(movie.getPosterPath());
        movieModel.setReleaseDate(movie.getReleaseDate());
        movieModel.setTitle(movie.getTitle());
        movieModel.setVideo(movie.isVideo());
        movieModel.setVoteAverage(movie.getVoteAverage());
        movieModel.setVoteCount(movie.getVoteCount());

        return movieModel;
    }

    public Collection<MovieModel> transform(Collection<Movie> movieCollection) {
        Collection<MovieModel> movieModelCollection;

        if (movieCollection != null && !movieCollection.isEmpty()) {
            movieModelCollection = new ArrayList<>();
            for (Movie movie : movieCollection) {
                movieModelCollection.add(transform(movie));
            }
        } else {
            movieModelCollection = Collections.emptyList();
        }

        return movieModelCollection;
    }
}
