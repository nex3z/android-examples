package com.nex3z.examlpes.mvp.presentation.internal.module;

import com.nex3z.examlpes.mvp.domain.executor.PostExecutionThread;
import com.nex3z.examlpes.mvp.domain.executor.ThreadExecutor;
import com.nex3z.examlpes.mvp.domain.interactor.GetMovieList;
import com.nex3z.examlpes.mvp.domain.interactor.UseCase;
import com.nex3z.examlpes.mvp.domain.repository.MovieRepository;
import com.nex3z.examlpes.mvp.presentation.internal.PerActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class MovieModule {

    @Provides @PerActivity
    UseCase provideGetMovieListUseCase(MovieRepository movieRepository,
                                       ThreadExecutor threadExecutor,
                                       PostExecutionThread postExecutionThread) {
        return new GetMovieList(movieRepository, threadExecutor, postExecutionThread);
    }

}
