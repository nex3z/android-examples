package com.nex3z.examples.dagger2.presenter;

import com.nex3z.examples.dagger2.model.Movie;

import java.util.List;

public interface OnDataReceivedListener {

    void onSuccess(List<Movie> movies);

    void onFailure(String message);

}
