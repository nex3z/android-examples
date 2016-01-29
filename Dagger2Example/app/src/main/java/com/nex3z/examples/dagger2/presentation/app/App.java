package com.nex3z.examples.dagger2.presentation.app;

import android.app.Application;

import com.nex3z.examples.dagger2.presentation.internal.component.*;
import com.nex3z.examples.dagger2.presentation.internal.module.AppModule;

public class App extends Application {
    private static final String BASE_URL = "http://api.themoviedb.org";

    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}
