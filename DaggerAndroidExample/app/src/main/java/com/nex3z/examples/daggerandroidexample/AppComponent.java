package com.nex3z.examples.daggerandroidexample;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Component(modules = { AndroidSupportInjectionModule.class, AppModule.class})
public interface AppComponent extends AndroidInjector<App> {
}
