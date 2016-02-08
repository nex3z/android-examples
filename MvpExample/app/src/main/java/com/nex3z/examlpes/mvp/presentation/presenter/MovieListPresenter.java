package com.nex3z.examlpes.mvp.presentation.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.nex3z.examlpes.mvp.domain.Movie;
import com.nex3z.examlpes.mvp.domain.exception.DefaultErrorBundle;
import com.nex3z.examlpes.mvp.domain.exception.ErrorBundle;
import com.nex3z.examlpes.mvp.domain.interactor.DefaultSubscriber;
import com.nex3z.examlpes.mvp.domain.interactor.GetMovieList;
import com.nex3z.examlpes.mvp.domain.interactor.UseCase;
import com.nex3z.examlpes.mvp.presentation.internal.PerActivity;
import com.nex3z.examlpes.mvp.presentation.mapper.MovieModelDataMapper;
import com.nex3z.examlpes.mvp.presentation.model.MovieModel;
import com.nex3z.examlpes.mvp.presentation.view.MovieListView;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

@PerActivity
public class MovieListPresenter implements Presenter {
    private static final String LOG_TAG = MovieListPresenter.class.getSimpleName();

    private MovieListView mMovieListView;

    private final UseCase mMovieListUseCase;
    private final MovieModelDataMapper mMovieModelDataMapper;

    @Inject
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

    public void initialize() {
        loadMovieList();
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

    public void getMovieList(int page) {
        Log.v(LOG_TAG, "getMovieList(): page = " + page);
        if (mMovieListUseCase instanceof GetMovieList) {
            ((GetMovieList) mMovieListUseCase).setPage(page);
        }
        mMovieListUseCase.execute(new MovieListSubscriber());
    }

    private void showMovieCollectionInView(Collection<Movie> movieCollection) {
        final Collection<MovieModel> movieModelCollection =
                mMovieModelDataMapper.transform(movieCollection);
        mMovieListView.renderMovieList(movieModelCollection);
    }

    private void appendMovieCollection(Collection<Movie> movieCollection) {
        Log.v(LOG_TAG, "appendMovieCollection(): movieCollection.size() = " + movieCollection.size());

        final Collection<MovieModel> movieModelCollection =
                mMovieModelDataMapper.transform(movieCollection);
        mMovieListView.appendMovieList(movieModelCollection);
    }

    private void loadMovieList() {
        showViewLoading();
        getMovieList();
    }

    private final class MovieListSubscriber extends DefaultSubscriber<List<Movie>> {
        private final String LOG_TAG = MovieListSubscriber.class.getSimpleName();

        @Override public void onCompleted() {
            Log.v(LOG_TAG, "onCompleted()");
            MovieListPresenter.this.hideViewLoading();
        }

        @Override public void onError(Throwable e) {
            Log.v(LOG_TAG, "onError(): e = " + e.getLocalizedMessage());
            MovieListPresenter.this.hideViewLoading();
            MovieListPresenter.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
        }

        @Override public void onNext(List<Movie> movies) {
            Log.v(LOG_TAG, "onNext(): movies.size() = " + movies.size());
            MovieListPresenter.this.appendMovieCollection(movies);
        }
    }

}
