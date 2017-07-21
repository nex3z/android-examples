package com.nex3z.examples.daggerandroidexample;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class AppModule {
    @ContributesAndroidInjector
    abstract MainActivity contributeActivityInjector();
}
