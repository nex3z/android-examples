package com.nex3z.examples.dagger2.ui.activity;

import android.support.v7.app.AppCompatActivity;

import com.nex3z.examples.dagger2.app.App;
import com.nex3z.examples.dagger2.internal.component.AppComponent;
import com.nex3z.examples.dagger2.internal.module.ActivityModule;


public class BaseActivity extends AppCompatActivity {

    protected AppComponent getApplicationComponent() {
        return ((App)getApplication()).getAppComponent();
    }

    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }

}
