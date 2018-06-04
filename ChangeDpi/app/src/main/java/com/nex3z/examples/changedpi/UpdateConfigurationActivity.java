package com.nex3z.examples.changedpi;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.TextView;

public class UpdateConfigurationActivity extends AppCompatActivity {
    private static final String LOG_TAG = UpdateConfigurationActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adjustDpiAuto();
        setContentView(R.layout.activity_update_configuration);

        TextView tvInfo = findViewById(R.id.tv_info);
        tvInfo.setText(DpiUtil.buildDpiInfo(this));
    }

    public void adjustDpiAuto() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Resources resources = getResources();
            Configuration config = resources.getConfiguration();
            config.densityDpi = DisplayMetrics.DENSITY_MEDIUM;
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            metrics.densityDpi = DisplayMetrics.DENSITY_MEDIUM;
            resources.updateConfiguration(config, metrics);
        }
    }

}
