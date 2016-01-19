package com.nex3z.examples.dagger2.interactor;


import android.util.Log;

import com.nex3z.examples.dagger2.model.Movie;
import com.nex3z.examples.dagger2.presenter.OnDataReceivedListener;
import com.nex3z.examples.dagger2.rest.service.MovieService;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MovieInteractorImpl implements MovieInteractor {
    private static final String LOG_TAG = MovieInteractorImpl.class.getSimpleName();

    MovieService mMovieService;

    public MovieInteractorImpl(MovieService movieService) {
        mMovieService = movieService;
    }

    @Override
    public void fetchMovies(final OnDataReceivedListener listener, int page) {
        Log.v(LOG_TAG, "fetchMovies(): page = " + page);

        mMovieService.getMovies(MovieService.SORT_BY_POPULARITY_DESC)
                .timeout(5, TimeUnit.SECONDS)
                .retry(2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            List<Movie> movies = response.getMovies();
                            Log.v(LOG_TAG, "onResponse(): movies size = " + movies.size());
                            listener.onSuccess(movies);
                        },
                        throwable -> {
                            listener.onFailure("Error: " + throwable.getLocalizedMessage());
                        }
                );
    }
}
