package com.nex3z.examples.dagger2.internal.component;

import com.nex3z.examples.dagger2.internal.PerActivity;
import com.nex3z.examples.dagger2.internal.module.MainModule;
import com.nex3z.examples.dagger2.internal.module.RestModule;
import com.nex3z.examples.dagger2.ui.activity.MainActivity;
import com.nex3z.examples.dagger2.ui.fragment.MainActivityFragment;

import dagger.Component;

@PerActivity
@Component(
        dependencies = AppComponent.class,
        modules = {MainModule.class, RestModule.class})
public interface MainComponent {

    MainActivity inject(MainActivity mainActivity);

    MainActivityFragment inject(MainActivityFragment mainActivityFragment);

}