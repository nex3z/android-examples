package com.nex3z.examples.multicastsocketexample.client;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String ADDRESS = "225.0.0.3";
    private static final int PORT = 8888;
    private EditText mEtMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEtMessage = (EditText) findViewById(R.id.et_text);

        Button broadcast = (Button) findViewById(R.id.btn_broadcast);
        broadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendService.startActionSend(MainActivity.this,
                        ADDRESS, PORT, mEtMessage.getText().toString());
            }
        });
    }

}
