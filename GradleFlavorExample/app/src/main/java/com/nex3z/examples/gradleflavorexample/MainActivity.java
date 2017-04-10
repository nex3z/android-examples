package com.nex3z.examples.gradleflavorexample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (BuildConfig.FLAVOR.equals("pro")) {
            TextView textView = (TextView) findViewById(R.id.tv_message);
            textView.setTextColor(Color.BLUE);
        }
    }
}
