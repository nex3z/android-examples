package com.nex3z.examples.dagger2.presentation.view;

import com.nex3z.examples.dagger2.presentation.model.MovieModel;

import java.util.Collection;

public interface MovieListView extends LoadDataView {

    void renderUserList(Collection<MovieModel> movieModelCollection);

}
