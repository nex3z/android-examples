package com.nex3z.examples.dagger2.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nex3z.examples.dagger2.BuildConfig;
import com.nex3z.examples.dagger2.rest.service.MovieService;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

@Module
public class RestModule {
    String mBaseUrl;

    public RestModule(String baseUrl) {
        mBaseUrl = baseUrl;
    }

    @Provides @Singleton
    Gson provideGson() {
        Gson gson = new GsonBuilder().create();
        return gson;
    }

    @Provides @Singleton
    OkHttpClient provideOkHttpClient() {
        OkHttpClient httpClient = new OkHttpClient();

        httpClient.interceptors().add(
                new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));

        httpClient.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
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

        return httpClient;
    }

    @Provides @Singleton
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit;
    }

    @Provides @Singleton
    MovieService provideMovieService(Retrofit retrofit) {
        MovieService movieService = retrofit.create(MovieService.class);
        return movieService;
    }

}
