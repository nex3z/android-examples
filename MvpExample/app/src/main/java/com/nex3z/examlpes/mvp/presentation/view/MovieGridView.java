package com.nex3z.examlpes.mvp.presentation.view;


import com.nex3z.examlpes.mvp.data.entity.MovieEntity;

import java.util.List;

public interface MovieGridView {

    void showProgress();

    void hideProgress();

    void appendMovies(List<MovieEntity> movies);

    void toastMessage(String message);

}
