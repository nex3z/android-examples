package com.nex3z.examples.dagger2.internal.module;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nex3z.examples.dagger2.BuildConfig;
import com.nex3z.examples.dagger2.internal.PerActivity;
import com.nex3z.examples.dagger2.rest.service.MovieService;

import java.io.IOException;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

    @Provides @PerActivity
    Cache provideOkHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024;
        Cache cache = new Cache(application.getCacheDir(), cacheSize);
        return cache;
    }

    @Provides @PerActivity
    OkHttpClient provideOkHttpClient(Cache cache) {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(
                        new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                .addInterceptor(
                        new Interceptor() {
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
        return httpClient;
    }

    @Provides @PerActivity
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit;
    }

    @Provides @PerActivity
    MovieService provideMovieService(Retrofit retrofit) {
        MovieService movieService = retrofit.create(MovieService.class);
        return movieService;
    }
}
