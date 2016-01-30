package com.nex3z.examlpes.mvp.domain.interactor;


import com.nex3z.examlpes.mvp.domain.executor.PostExecutionThread;
import com.nex3z.examlpes.mvp.domain.executor.ThreadExecutor;
import com.nex3z.examlpes.mvp.domain.repository.MovieRepository;

import rx.Observable;

public class GetMovieList extends UseCase {

    private final MovieRepository mMovieRepository;

    private int mPage = 1;

    public GetMovieList(MovieRepository movieRepository, ThreadExecutor threadExecutor,
                        PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mMovieRepository = movieRepository;
    }

    public void setPage(int page) {
        mPage = page;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return mMovieRepository.movies(mPage);
    }
}
