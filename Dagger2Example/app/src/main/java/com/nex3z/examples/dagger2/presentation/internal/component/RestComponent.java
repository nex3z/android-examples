package com.nex3z.examples.dagger2.presentation.internal.component;

import com.nex3z.examples.dagger2.presentation.internal.PerActivity;
import com.nex3z.examples.dagger2.presentation.internal.module.RestModule;

import dagger.Component;

@PerActivity
@Component(dependencies = AppComponent.class, modules = RestModule.class)
public interface RestComponent {
    // void inject(MainActivity activity);
    // void inject(MainActivityFragment fragment);
}
