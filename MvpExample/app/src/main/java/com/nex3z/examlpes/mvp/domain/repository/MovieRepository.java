package com.nex3z.examlpes.mvp.domain.repository;


import com.nex3z.examlpes.mvp.domain.Movie;

import java.util.List;

import rx.Observable;

public interface MovieRepository {

    Observable<List<Movie>> movies(int page);

}
