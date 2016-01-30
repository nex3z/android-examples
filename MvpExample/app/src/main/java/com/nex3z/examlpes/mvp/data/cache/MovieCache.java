package com.nex3z.examlpes.mvp.data.cache;


import com.nex3z.examlpes.mvp.data.entity.MovieEntity;

import rx.Observable;

public interface MovieCache {

    Observable<MovieEntity> get(final int movieId);

    void put(MovieEntity movieEntity);

    boolean isCached(final int movieId);

    boolean isExpired();

    void evictAll();
}
