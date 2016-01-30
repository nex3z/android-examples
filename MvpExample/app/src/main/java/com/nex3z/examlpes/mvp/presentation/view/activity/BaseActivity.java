package com.nex3z.examlpes.mvp.presentation.view.activity;

import android.support.v7.app.AppCompatActivity;

import com.nex3z.examlpes.mvp.presentation.app.App;
import com.nex3z.examlpes.mvp.presentation.internal.component.AppComponent;


public class BaseActivity extends AppCompatActivity {

    protected AppComponent getApplicationComponent() {
        return ((App)getApplication()).getAppComponent();
    }

}
