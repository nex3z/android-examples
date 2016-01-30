package com.nex3z.examlpes.mvp.presentation.presenter;


import android.util.Log;

import com.nex3z.examlpes.mvp.data.entity.MovieEntity;
import com.nex3z.examlpes.mvp.domain.interactor.MovieInteractor;
import com.nex3z.examlpes.mvp.presentation.view.MovieGridView;

import java.util.List;

public class MainPresenterImpl implements MainPresenter, OnDataReceivedListener {
    private static final String LOG_TAG = MainPresenterImpl.class.getSimpleName();

    private MovieGridView mMainView;
    private MovieInteractor mMovieInteractor;

    public MainPresenterImpl(MovieGridView mainView, MovieInteractor movieInteractor) {
        mMainView = mainView;
        mMovieInteractor = movieInteractor;
    }

    @Override
    public void fetchMovies(int page) {
        Log.v(LOG_TAG, "fetchMovies(): page = " + page);
        mMainView.showProgress();
        mMovieInteractor.fetchMovies(this, page);
    }

    @Override
    public void onFailure(String message) {
        Log.v(LOG_TAG, "onFailure(): message = " + message);
        mMainView.hideProgress();
        mMainView.toastMessage(message);
    }

    @Override
    public void onSuccess(List<MovieEntity> movies) {
        Log.v(LOG_TAG, "onSuccess(): movies.size() = " + movies.size());
        mMainView.hideProgress();
        mMainView.appendMovies(movies);
    }
}
