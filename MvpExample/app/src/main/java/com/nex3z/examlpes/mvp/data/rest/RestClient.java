package com.nex3z.examlpes.mvp.data.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nex3z.examlpes.mvp.BuildConfig;
import com.nex3z.examlpes.mvp.data.rest.service.MovieService;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.io.IOException;

import javax.inject.Inject;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

public class RestClient {
    private static final String BASE_URL = "http://api.themoviedb.org";
    private MovieService mMovieService;

    @Inject
    public RestClient() {
        Gson gson = new GsonBuilder().create();

        OkHttpClient httpClient = new OkHttpClient();

        httpClient.interceptors().add(
                new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));

        httpClient.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                HttpUrl originalHttpUrl = original.httpUrl();

                HttpUrl.Builder builder = originalHttpUrl.newBuilder()
                        .addQueryParameter("api_key", BuildConfig.API_KEY);

                Request.Builder requestBuilder = original.newBuilder()
                        .url(builder.build())
                        .method(original.method(), original.body());

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        mRetrofit = retrofit;
        mMovieService = retrofit.create(MovieService.class);
    }

    public MovieService getMovieService() { return mMovieService; }

    private Retrofit mRetrofit;

    public Retrofit getRetrofit() {
        return mRetrofit;
    }


}
