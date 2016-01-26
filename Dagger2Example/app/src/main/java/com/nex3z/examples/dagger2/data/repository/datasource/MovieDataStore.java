package com.nex3z.examples.dagger2.data.repository.datasource;

import com.nex3z.examples.dagger2.data.entity.MovieEntity;

import java.util.List;

import rx.Observable;

public interface MovieDataStore {

    Observable<List<MovieEntity>> movieEntityList(int page);

}
