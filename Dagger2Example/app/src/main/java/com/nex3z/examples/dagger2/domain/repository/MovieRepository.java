package com.nex3z.examples.dagger2.domain.repository;

import com.nex3z.examples.dagger2.domain.Movie;

import java.util.List;

import rx.Observable;

public interface MovieRepository {

    Observable<List<Movie>> movies(int page);

}
