package com.nex3z.examples.dagger2.view;


import com.nex3z.examples.dagger2.model.Movie;

import java.util.List;

public interface MovieGridView {

    void showProgress();

    void hideProgress();

    void appendMovies(List<Movie> movies);

    void toastMessage(String message);

}
