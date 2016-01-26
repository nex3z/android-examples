package com.nex3z.examples.dagger2.data.repository.datasource;

import com.nex3z.examples.dagger2.data.cache.MovieCache;
import com.nex3z.examples.dagger2.data.entity.MovieEntity;
import com.nex3z.examples.dagger2.data.rest.service.MovieService;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;

public class CloudMovieDataStore implements MovieDataStore {

    private final MovieService mMovieService;
    private final MovieCache mMovieCache;

    public CloudMovieDataStore(MovieService movieService, MovieCache movieCache) {
        mMovieService = movieService;
        mMovieCache = movieCache;
    }

    private final Action1<MovieEntity> saveToCacheAction = userEntity -> {
        if (userEntity != null) {
            CloudMovieDataStore.this.mMovieCache.put(userEntity);
        }
    };

    @Override
    public Observable<List<MovieEntity>> movieEntityList(int page) {
        return mMovieService
                .getMovies(MovieService.SORT_BY_POPULARITY_DESC, page)
                .map(response -> response.getMovies());
    }
}
