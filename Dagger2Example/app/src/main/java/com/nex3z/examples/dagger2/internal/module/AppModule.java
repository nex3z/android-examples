package com.nex3z.examples.dagger2.internal.module;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    Application mApplication;

    public AppModule(Application application) {
        mApplication = application;
    }

    @Provides @Singleton
    Application providesApplication() {
        return mApplication;
    }

    @Provides @Singleton
    Context provideApplicationContext() {
        return mApplication;
    }

}
