package com.nex3z.examlpes.mvp.presentation.internal.component;

import com.nex3z.examlpes.mvp.presentation.internal.PerActivity;
import com.nex3z.examlpes.mvp.presentation.internal.module.ActivityModule;
import com.nex3z.examlpes.mvp.presentation.internal.module.MovieModule;
import com.nex3z.examlpes.mvp.presentation.view.fragment.MainActivityFragment;

import dagger.Component;

@PerActivity
@Component(dependencies = AppComponent.class, modules = {ActivityModule.class, MovieModule.class})
public interface MovieComponent extends ActivityComponent {

    void inject(MainActivityFragment mainActivityFragment);

}
