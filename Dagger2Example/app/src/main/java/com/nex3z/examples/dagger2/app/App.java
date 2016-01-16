package com.nex3z.examples.dagger2.app;

import android.app.Application;

import com.nex3z.examples.dagger2.rest.DaggerRestComponent;
import com.nex3z.examples.dagger2.rest.RestComponent;
import com.nex3z.examples.dagger2.rest.RestModule;

public class App extends Application {
    private static final String BASE_URL = "http://api.themoviedb.org";
    private RestComponent mRestComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mRestComponent = DaggerRestComponent.builder()
                .restModule(new RestModule(BASE_URL))
                .build();
    }

    public RestComponent getRestComponent() {
        return mRestComponent;
    }
}
