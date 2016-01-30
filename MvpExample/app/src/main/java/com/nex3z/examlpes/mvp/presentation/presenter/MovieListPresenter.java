package com.nex3z.examlpes.mvp.presentation.presenter;

import android.support.annotation.NonNull;

import com.nex3z.examlpes.mvp.domain.Movie;
import com.nex3z.examlpes.mvp.domain.exception.DefaultErrorBundle;
import com.nex3z.examlpes.mvp.domain.exception.ErrorBundle;
import com.nex3z.examlpes.mvp.domain.interactor.DefaultSubscriber;
import com.nex3z.examlpes.mvp.domain.interactor.UseCase;
import com.nex3z.examlpes.mvp.presentation.mapper.MovieModelDataMapper;
import com.nex3z.examlpes.mvp.presentation.model.MovieModel;
import com.nex3z.examlpes.mvp.presentation.view.MovieListView;

import java.util.Collection;
import java.util.List;

public class MovieListPresenter implements Presenter {

    private MovieListView mMovieListView;

    private final UseCase mMovieListUseCase;
    private final MovieModelDataMapper mMovieModelDataMapper;

    public MovieListPresenter(UseCase movieListUseCase,
                              MovieModelDataMapper movieModelDataMapper) {
        mMovieListUseCase = movieListUseCase;
        mMovieModelDataMapper = movieModelDataMapper;
    }

    public void setView(@NonNull MovieListView movieListView) {
        mMovieListView = movieListView;
    }

    @Override
    public void destroy() {
        mMovieListUseCase.unsubscribe();
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {
    }

    private void showViewLoading() {
        mMovieListView.showLoading();
    }

    private void hideViewLoading() {
        mMovieListView.hideLoading();
    }

    private void showErrorMessage(ErrorBundle errorBundle) {
        String errorMessage = errorBundle.getErrorMessage();
        mMovieListView.showError(errorMessage);
    }

    private void getMovieList() {
        mMovieListUseCase.execute(new MovieListSubscriber());
    }

    private void showMovieCollectionInView(Collection<Movie> movieCollection) {
        final Collection<MovieModel> movieModelCollection =
                mMovieModelDataMapper.transform(movieCollection);
        mMovieListView.renderUserList(movieModelCollection);
    }

    private void loadMovieList() {
        showViewLoading();
        getMovieList();
    }

    private final class MovieListSubscriber extends DefaultSubscriber<List<Movie>> {

        @Override public void onCompleted() {
            MovieListPresenter.this.hideViewLoading();
        }

        @Override public void onError(Throwable e) {
            MovieListPresenter.this.hideViewLoading();
            MovieListPresenter.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
        }

        @Override public void onNext(List<Movie> movies) {
            MovieListPresenter.this.showMovieCollectionInView(movies);
        }
    }

}
