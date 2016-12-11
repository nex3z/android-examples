package com.nex3z.examples.dagger2.app;

import android.app.Application;

import com.nex3z.examples.dagger2.internal.component.AppComponent;
import com.nex3z.examples.dagger2.internal.component.DaggerAppComponent;
import com.nex3z.examples.dagger2.internal.module.AppModule;

public class App extends Application {
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
