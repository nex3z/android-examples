package com.nex3z.examples.retrofit2.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nex3z.examples.retrofit2.BuildConfig;
import com.nex3z.examples.retrofit2.rest.service.MovieService;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {
    private static final String BASE_URL = "http://api.themoviedb.org";
    private MovieService mMovieService;

    public RestClient() {
        Gson gson = new GsonBuilder().create();

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(
                        new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        HttpUrl originalHttpUrl = original.url();

                        HttpUrl.Builder builder = originalHttpUrl.newBuilder()
                                .addQueryParameter("api_key", BuildConfig.API_KEY);

                        Request.Builder requestBuilder = original.newBuilder()
                                .url(builder.build())
                                .method(original.method(), original.body());

                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                    }
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        mMovieService = retrofit.create(MovieService.class);
    }

    public MovieService getMovieService() { return mMovieService; }
}
