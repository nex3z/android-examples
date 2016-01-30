package com.nex3z.examlpes.mvp.domain.interactor;


import com.nex3z.examlpes.mvp.presentation.presenter.OnDataReceivedListener;

public interface MovieInteractor {

    void fetchMovies(OnDataReceivedListener listener, int page);

}
