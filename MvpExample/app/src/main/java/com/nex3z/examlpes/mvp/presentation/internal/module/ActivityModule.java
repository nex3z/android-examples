package com.nex3z.examlpes.mvp.presentation.internal.module;

import android.app.Activity;

import com.nex3z.examlpes.mvp.presentation.internal.PerActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {
    private Activity mActivity;

    public ActivityModule(Activity activity) {
        mActivity = activity;
    }

    @Provides @PerActivity
    Activity provideActivity() {
        return mActivity;
    }

}
