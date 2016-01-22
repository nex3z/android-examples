package com.nex3z.examples.dagger2.view.activity;

import android.support.v7.app.AppCompatActivity;

import com.nex3z.examples.dagger2.app.App;
import com.nex3z.examples.dagger2.internal.component.AppComponent;


public class BaseActivity extends AppCompatActivity {

    protected AppComponent getApplicationComponent() {
        return ((App)getApplication()).getAppComponent();
    }

}
