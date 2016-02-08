package com.nex3z.examlpes.mvp.data.entity.mapper;

import com.nex3z.examlpes.mvp.data.entity.MovieEntity;
import com.nex3z.examlpes.mvp.domain.Movie;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MovieEntityDataMapper {

    @Inject
    public MovieEntityDataMapper() { }

    public Movie transform(MovieEntity movieEntity) {
        Movie movie = null;
        if (movieEntity != null) {
            movie = new Movie();
            movie.setAdult(movieEntity.isAdult());
            movie.setBackdropPath(movieEntity.getBackdropPath());
            movie.setGenreIds(movieEntity.getGenreIds());
            movie.setId(movieEntity.getId());
            movie.setOriginalLanguage(movieEntity.getOriginalLanguage());
            movie.setOriginalTitle(movieEntity.getOriginalTitle());
            movie.setOverview(movieEntity.getOverview());
            movie.setPopularity(movieEntity.getPopularity());
            movie.setPosterPath(movieEntity.getPosterPath());
            movie.setReleaseDate(movieEntity.getReleaseDate());
            movie.setTitle(movieEntity.getTitle());
            movie.setVideo(movieEntity.isVideo());
            movie.setVoteAverage(movieEntity.getVoteAverage());
            movie.setVoteCount(movieEntity.getVoteCount());
        }
        return movie;
    }

    public List<Movie> transform(Collection<MovieEntity> movieEntityCollection) {
        List<Movie> movieList = new ArrayList<>(20);
        Movie movie;
        for (MovieEntity movieEntity : movieEntityCollection) {
            movie = transform(movieEntity);
            if (movie != null) {
                movieList.add(movie);
            }
        }
        return movieList;
    }
}
