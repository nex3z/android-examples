package com.nex3z.examples.changedpi;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class CreateConfigurationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_configuration);

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.vg_container, CreateConfigurationFragment.newInstance());
            transaction.commit();
        }

        TextView tvInfo = findViewById(R.id.tv_info);
        tvInfo.setText(DpiUtil.buildDpiInfo(this));
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(DpiContextWrapper.wrap(newBase));
    }
}
