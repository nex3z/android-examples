package com.nex3z.examlpes.mvp.presentation.app;

import android.app.Application;

import com.nex3z.examlpes.mvp.presentation.internal.component.AppComponent;

import com.nex3z.examlpes.mvp.presentation.internal.component.DaggerAppComponent;
import com.nex3z.examlpes.mvp.presentation.internal.module.AppModule;


public class App extends Application {
    private static final String BASE_URL = "http://api.themoviedb.org";

    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeInjector();
    }

    private void initializeInjector() {
        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}
