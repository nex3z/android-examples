package com.nex3z.examples.dagger2.internal.component;

import android.app.Activity;

import com.nex3z.examples.dagger2.internal.PerActivity;
import com.nex3z.examples.dagger2.internal.module.ActivityModule;

import dagger.Component;

@PerActivity
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    Activity activity();
}