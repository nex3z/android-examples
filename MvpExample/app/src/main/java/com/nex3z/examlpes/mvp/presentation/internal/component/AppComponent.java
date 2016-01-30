package com.nex3z.examlpes.mvp.presentation.internal.component;


import android.app.Application;
import android.content.Context;

import com.nex3z.examlpes.mvp.presentation.internal.module.AppModule;
import com.nex3z.examlpes.mvp.presentation.view.activity.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(MainActivity activity);

    Context context();
    Application application();

}
