package com.nex3z.examples.dagger2.domain.interactor;

import com.nex3z.examples.dagger2.presentation.presenter.OnDataReceivedListener;

public interface MovieInteractor {

    void fetchMovies(OnDataReceivedListener listener, int page);

}
