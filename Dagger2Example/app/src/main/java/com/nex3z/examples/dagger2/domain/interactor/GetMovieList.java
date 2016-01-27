package com.nex3z.examples.dagger2.domain.interactor;

import com.nex3z.examples.dagger2.domain.executor.PostExecutionThread;
import com.nex3z.examples.dagger2.domain.executor.ThreadExecutor;
import com.nex3z.examples.dagger2.domain.repository.MovieRepository;

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
