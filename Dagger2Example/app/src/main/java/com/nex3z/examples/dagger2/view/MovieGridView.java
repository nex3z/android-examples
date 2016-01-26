package com.nex3z.examples.dagger2.view;

import com.nex3z.examples.dagger2.data.entity.MovieEntity;

import java.util.List;

public interface MovieGridView {

    void showProgress();

    void hideProgress();

    void appendMovies(List<MovieEntity> movies);

    void toastMessage(String message);

}
