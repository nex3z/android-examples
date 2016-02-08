package com.nex3z.examlpes.mvp.presentation.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.nex3z.examlpes.mvp.presentation.app.App;
import com.nex3z.examlpes.mvp.presentation.internal.component.AppComponent;
import com.nex3z.examlpes.mvp.presentation.internal.module.ActivityModule;


public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getApplicationComponent().inject(this);
    }

    protected AppComponent getApplicationComponent() {
        return ((App)getApplication()).getAppComponent();
    }

    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }

}
