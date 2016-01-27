package com.nex3z.examples.dagger2.data.repository.datasource;

import com.nex3z.examples.dagger2.data.rest.service.MovieService;

import retrofit.Retrofit;

public class MovieDataStoreFactory {

    private final Retrofit mRetrofit;

    public MovieDataStoreFactory(Retrofit retrofit) {
        mRetrofit = retrofit;
    }

    public MovieDataStore createCloudDataStore() {
        MovieService movieService = mRetrofit.create(MovieService.class);
        return new CloudMovieDataStore(movieService);
    }

}
