package com.nex3z.examples.dagger2.internal.module;

import android.app.Activity;

import com.nex3z.examples.dagger2.internal.PerActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {
    private final Activity mActivity;

    public ActivityModule(Activity activity) {
        this.mActivity = activity;
    }

    @Provides
    @PerActivity
    Activity activity() {
        return this.mActivity;
    }
}