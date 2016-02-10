package com.nex3z.examples.barcode;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final int SCAN_REQUEST_REQUEST = 1;

    private Button mBtnScan;
    private TextView mTxtResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnScan = (Button) findViewById(R.id.btn_scan);
        mBtnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ScanActivity.class);
                startActivityForResult(intent, SCAN_REQUEST_REQUEST);
            }
        });

        mTxtResult = (TextView) findViewById(R.id.txt_result);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SCAN_REQUEST_REQUEST) {
            if(resultCode == Activity.RESULT_OK){
                String result = data.getStringExtra(ScanActivity.SCAN_RESULT);
                mTxtResult.setText(result);
            }
        }
    }

}
