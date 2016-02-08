package com.nex3z.examlpes.mvp.data.repository.datasource;


import com.nex3z.examlpes.mvp.data.rest.service.MovieService;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit.Retrofit;

@Singleton
public class MovieDataStoreFactory {

    private final Retrofit mRetrofit;

    @Inject
    public MovieDataStoreFactory(Retrofit retrofit) {
        mRetrofit = retrofit;
    }

    public MovieDataStore createCloudDataStore() {
        MovieService movieService = mRetrofit.create(MovieService.class);
        return new CloudMovieDataStore(movieService);
    }

}
