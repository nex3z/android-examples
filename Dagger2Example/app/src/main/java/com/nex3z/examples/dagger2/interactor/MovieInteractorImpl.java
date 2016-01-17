package com.nex3z.examples.dagger2.interactor;


import android.util.Log;

import com.nex3z.examples.dagger2.model.Movie;
import com.nex3z.examples.dagger2.presenter.OnDataReceivedListener;
import com.nex3z.examples.dagger2.rest.model.MovieResponse;
import com.nex3z.examples.dagger2.rest.service.MovieService;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MovieInteractorImpl implements MovieInteractor {
    private static final String LOG_TAG = MovieInteractorImpl.class.getSimpleName();

    MovieService mMovieService;

    public MovieInteractorImpl(MovieService movieService) {
        mMovieService = movieService;
    }

    @Override
    public void fetchMovies(final OnDataReceivedListener listener, int page) {
        Log.v(LOG_TAG, "fetchMovies(): page = " + page);

        Call<MovieResponse> mCall = mMovieService.getMovies(
                MovieService.SORT_BY_POPULARITY_DESC, page);

        mCall.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Response<MovieResponse> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    MovieResponse movieResponse = response.body();
                    List<Movie> movies = movieResponse.getMovies();
                    Log.v(LOG_TAG, "onResponse(): movies size = " + movies.size());
                    listener.onSuccess(movies);
                } else {
                    int statusCode = response.code();
                    Log.e(LOG_TAG, "onResponse(): Error code = " + statusCode);
                    listener.onFailure("Error code = " + statusCode);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });
    }
}
