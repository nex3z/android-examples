package com.nex3z.examples.changedpi;

import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        Button btnUpdate = findViewById(R.id.btn_update);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UpdateConfigurationActivity.class);
                startActivity(intent);
            }
        });

        Button btnCreate = findViewById(R.id.btn_create);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateConfigurationActivity.class);
                startActivity(intent);
            }
        });

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float density = metrics.density;
        int dpi = (int)(density * 160);
        Point point = new Point();
        Display display = getWindowManager().getDefaultDisplay();
        display.getRealSize(point);
        int widthPx = point.x;
        int heightPx = point.y;
        int widthDp = (int)(widthPx / density);
        int heightDp = (int)(heightPx / density);

        TextView tvInfo = findViewById(R.id.tv_info);
        tvInfo.setText(String.format(getString(R.string.info), density, dpi, heightPx, widthPx,
                heightDp, widthDp));
    }

}
