package com.nex3z.boundserviceexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.nex3z.boundserviceexample.aidl.AidlActivity;
import com.nex3z.boundserviceexample.messenger.MessengerActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnMessengerService = (Button) findViewById(R.id.btn_messenger_service);
        btnMessengerService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MessengerActivity.class);
                startActivity(intent);
            }
        });

        Button btnAidlService = (Button) findViewById(R.id.btn_aidl_service);
        btnAidlService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AidlActivity.class);
                startActivity(intent);
            }
        });

    }


}
