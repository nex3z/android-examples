package com.nex3z.examples.daggerandroidexample;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@Subcomponent()
public interface MainActivitySubcomponent extends AndroidInjector<MainActivity> {

    @Subcomponent.Builder
    public abstract class Builder extends AndroidInjector.Builder<MainActivity> {}

}