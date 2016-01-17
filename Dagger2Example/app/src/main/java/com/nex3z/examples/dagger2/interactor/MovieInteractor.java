package com.nex3z.examples.dagger2.interactor;

import com.nex3z.examples.dagger2.presenter.OnDataReceivedListener;

public interface MovieInteractor {

    void fetchMovies(OnDataReceivedListener listener, int page);

}
