package com.nex3z.examlpes.mvp.presentation.internal.component;


import com.nex3z.examlpes.mvp.presentation.internal.PerActivity;
import com.nex3z.examlpes.mvp.presentation.internal.module.MainModule;
import com.nex3z.examlpes.mvp.presentation.internal.module.RestModule;
import com.nex3z.examlpes.mvp.presentation.view.activity.MainActivity;
import com.nex3z.examlpes.mvp.presentation.view.fragment.MainActivityFragment;

import dagger.Component;

@PerActivity
@Component(
        dependencies = AppComponent.class,
        modules = {MainModule.class, RestModule.class})
public interface MainComponent {

    MainActivity inject(MainActivity mainActivity);

    MainActivityFragment inject(MainActivityFragment mainActivityFragment);

}