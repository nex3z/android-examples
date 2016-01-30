package com.nex3z.examlpes.mvp.presentation.internal.module;


import com.nex3z.examlpes.mvp.data.rest.service.MovieService;
import com.nex3z.examlpes.mvp.domain.interactor.MovieInteractor;
import com.nex3z.examlpes.mvp.domain.interactor.MovieInteractorImpl;
import com.nex3z.examlpes.mvp.presentation.internal.PerActivity;
import com.nex3z.examlpes.mvp.presentation.presenter.MainPresenter;
import com.nex3z.examlpes.mvp.presentation.presenter.MainPresenterImpl;
import com.nex3z.examlpes.mvp.presentation.view.MovieGridView;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {
    private MovieGridView mMovieGridView;

    public MainModule(MovieGridView movieGridView) {
        mMovieGridView = movieGridView;
    }

    @Provides @PerActivity
    MovieGridView provideMovieGridView() {
        return mMovieGridView;
    }

    @Provides @PerActivity
    MovieInteractor provideMovieInteractor(MovieService movieService) {
        return new MovieInteractorImpl(movieService);
    }

    @Provides @PerActivity
    MainPresenter provideMainPresenter(MovieGridView movieGridView,
                                       MovieInteractor movieInteractor) {
        return new MainPresenterImpl(movieGridView, movieInteractor);
    }
}