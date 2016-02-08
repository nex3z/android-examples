package com.nex3z.examlpes.mvp.presentation.internal.component;

import android.app.Activity;

import com.nex3z.examlpes.mvp.presentation.internal.PerActivity;
import com.nex3z.examlpes.mvp.presentation.internal.module.ActivityModule;

import dagger.Component;

@PerActivity
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    Activity activity();
}