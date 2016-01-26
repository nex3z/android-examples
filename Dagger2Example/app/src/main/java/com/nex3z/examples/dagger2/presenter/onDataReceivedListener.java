package com.nex3z.examples.dagger2.presenter;


import com.nex3z.examples.dagger2.data.entity.MovieEntity;

import java.util.List;

public interface OnDataReceivedListener {

    void onSuccess(List<MovieEntity> movies);

    void onFailure(String message);

}
