package com.nex3z.examples.dagger2.internal.component;

import android.app.Application;
import android.content.Context;

import com.nex3z.examples.dagger2.internal.module.AppModule;
import com.nex3z.examples.dagger2.ui.activity.MovieGridActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(MovieGridActivity activity);

    Context context();
    Application application();
}
