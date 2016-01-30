package com.nex3z.examlpes.mvp.presentation.view;


import com.nex3z.examlpes.mvp.presentation.model.MovieModel;

import java.util.Collection;

public interface MovieListView extends LoadDataView {

    void renderUserList(Collection<MovieModel> movieModelCollection);

}
