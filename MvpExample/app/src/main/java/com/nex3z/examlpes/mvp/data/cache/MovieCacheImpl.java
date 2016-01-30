package com.nex3z.examlpes.mvp.data.cache;


import com.nex3z.examlpes.mvp.data.entity.MovieEntity;

import rx.Observable;

public class MovieCacheImpl implements MovieCache {

    @Override
    public Observable<MovieEntity> get(int movieId) {
        return null;
    }

    @Override
    public void put(MovieEntity movieEntity) {

    }

    @Override
    public boolean isCached(int movieId) {
        return false;
    }

    @Override
    public boolean isExpired() {
        return false;
    }

    @Override
    public void evictAll() {

    }
}
