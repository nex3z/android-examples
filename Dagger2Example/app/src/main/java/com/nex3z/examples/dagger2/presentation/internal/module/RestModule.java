package com.nex3z.examples.dagger2.presentation.internal.module;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nex3z.examples.dagger2.BuildConfig;
import com.nex3z.examples.dagger2.presentation.internal.PerActivity;
import com.nex3z.examples.dagger2.data.rest.service.MovieService;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.io.IOException;

import dagger.Module;
import dagger.Provides;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

@Module
public class RestModule {
    String mBaseUrl;

    public RestModule(String baseUrl) {
        mBaseUrl = baseUrl;
    }

    @Provides @PerActivity
    Gson provideGson() {
        Gson gson = new GsonBuilder().create();
        return gson;
    }

    @Provides
    @PerActivity
    Cache provideOkHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024;
        Cache cache = new Cache(application.getCacheDir(), cacheSize);
        return cache;
    }

    @Provides @PerActivity
    OkHttpClient provideOkHttpClient(Cache cache) {
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

        httpClient.setCache(cache);

        return httpClient;
    }

    @Provides @PerActivity
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit;
    }

    @Provides @PerActivity
    MovieService provideMovieService(Retrofit retrofit) {
        MovieService movieService = retrofit.create(MovieService.class);
        return movieService;
    }

}
