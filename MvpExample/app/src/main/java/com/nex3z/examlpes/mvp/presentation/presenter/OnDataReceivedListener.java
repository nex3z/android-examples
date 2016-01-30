package com.nex3z.examlpes.mvp.presentation.presenter;


import com.nex3z.examlpes.mvp.data.entity.MovieEntity;

import java.util.List;

public interface OnDataReceivedListener {

    void onSuccess(List<MovieEntity> movies);

    void onFailure(String message);

}
