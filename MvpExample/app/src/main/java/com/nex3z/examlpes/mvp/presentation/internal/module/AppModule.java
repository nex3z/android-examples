package com.nex3z.examlpes.mvp.presentation.internal.module;

import android.app.Application;
import android.content.Context;

import com.nex3z.examlpes.mvp.data.executor.JobExecutor;
import com.nex3z.examlpes.mvp.data.repository.MovieDataRepository;
import com.nex3z.examlpes.mvp.data.rest.RestClient;
import com.nex3z.examlpes.mvp.domain.executor.PostExecutionThread;
import com.nex3z.examlpes.mvp.domain.executor.ThreadExecutor;
import com.nex3z.examlpes.mvp.domain.repository.MovieRepository;
import com.nex3z.examlpes.mvp.presentation.UIThread;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.Retrofit;

@Module
public class AppModule {
    Application mApplication;

    public AppModule(Application application) {
        mApplication = application;
    }

    @Provides @Singleton
    Application provideApplication() {
        return mApplication;
    }

    @Provides @Singleton
    Context provideApplicationContext() {
        return mApplication;
    }

    @Provides @Singleton
    ThreadExecutor provideThreadExecutor(JobExecutor jobExecutor) {
        return jobExecutor;
    }

    @Provides @Singleton
    PostExecutionThread providePostExecutionThread(UIThread uiThread) {
        return uiThread;
    }

    @Provides @Singleton
    MovieRepository provideMovieRepository(MovieDataRepository movieDataRepository) {
        return movieDataRepository;
    }

    @Provides @Singleton
    Retrofit provideRetrofit(RestClient restClient) {
        return restClient.getRetrofit();
    }
}

