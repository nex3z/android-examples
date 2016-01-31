package com.nex3z.examlpes.mvp.data.repository;


import android.util.Log;

import com.nex3z.examlpes.mvp.data.entity.mapper.MovieEntityDataMapper;
import com.nex3z.examlpes.mvp.data.repository.datasource.MovieDataStore;
import com.nex3z.examlpes.mvp.data.repository.datasource.MovieDataStoreFactory;
import com.nex3z.examlpes.mvp.domain.Movie;
import com.nex3z.examlpes.mvp.domain.repository.MovieRepository;

import java.util.List;

import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class MovieDataRepository implements MovieRepository {
    private static final String LOG_TAG = MovieDataRepository.class.getSimpleName();

    private final MovieDataStoreFactory mMovieDataStoreFactory;
    private final MovieEntityDataMapper mMovieEntityDataMapper;

    public MovieDataRepository(MovieDataStoreFactory movieDataStoreFactory,
                               MovieEntityDataMapper movieEntityDataMapper) {
        mMovieDataStoreFactory = movieDataStoreFactory;
        mMovieEntityDataMapper = movieEntityDataMapper;
    }


    @Override
    public Observable<List<Movie>> movies(int page) {
        Log.v(LOG_TAG, "movies(): page = " + page);
        final MovieDataStore movieDataStore = mMovieDataStoreFactory.createCloudDataStore();
        return movieDataStore
                .movieEntityList(page)
                .map(movieEntities -> mMovieEntityDataMapper.transform(movieEntities));
    }
}
