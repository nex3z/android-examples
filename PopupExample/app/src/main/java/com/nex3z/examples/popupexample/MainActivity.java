package com.nex3z.examples.popupexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private final String[] data = {"A", "B", "C", "D"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnShowPopupWindow = (Button) findViewById(R.id.btn_show_popup_window_demo);
        btnShowPopupWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PopupWindowDemoActivity.class);
                startActivity(intent);
            }
        });

        Button btnShowAlertDialog = (Button) findViewById(R.id.btn_show_alert_dialog_demo);
        btnShowAlertDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AlertDialogDemoActivity.class);
                startActivity(intent);
            }
        });
    }

}
