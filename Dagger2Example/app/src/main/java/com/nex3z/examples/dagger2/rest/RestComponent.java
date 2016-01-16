package com.nex3z.examples.dagger2.rest;

import com.nex3z.examples.dagger2.ui.activity.MainActivity;
import com.nex3z.examples.dagger2.ui.fragment.MainActivityFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules={RestModule.class})
public interface RestComponent {
    void inject(MainActivity activity);
    void inject(MainActivityFragment fragment);
}
