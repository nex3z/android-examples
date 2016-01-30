package com.nex3z.examlpes.mvp.data.repository.datasource;


import com.nex3z.examlpes.mvp.data.entity.MovieEntity;
import com.nex3z.examlpes.mvp.data.rest.service.MovieService;

import java.util.List;

import rx.Observable;

public class CloudMovieDataStore implements MovieDataStore {

    private final MovieService mMovieService;

    public CloudMovieDataStore(MovieService movieService) {
        mMovieService = movieService;
    }

    @Override
    public Observable<List<MovieEntity>> movieEntityList(int page) {
        return mMovieService
                .getMovies(MovieService.SORT_BY_POPULARITY_DESC, page)
                .map(response -> response.getMovies());
    }
}
