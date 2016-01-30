package com.nex3z.examlpes.mvp.data.repository.datasource;


import com.nex3z.examlpes.mvp.data.entity.MovieEntity;

import java.util.List;

import rx.Observable;

public interface MovieDataStore {

    Observable<List<MovieEntity>> movieEntityList(int page);

}
