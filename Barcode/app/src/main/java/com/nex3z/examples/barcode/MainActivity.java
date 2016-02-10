package com.nex3z.examples.barcode;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final int SCAN_REQUEST_REQUEST = 1;
    private static final String CLIP_LABEL = "scan_result";

    private Button mBtnScan;
    private Button mBtnCopy;
    private TextView mTxtResult;
    private String mResult = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnScan = (Button) findViewById(R.id.btn_scan);
        mBtnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScanner();
            }
        });

        mBtnCopy = (Button) findViewById(R.id.btn_copy);
        mBtnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mResult != null && mResult != "") {
                    ClipboardManager clipboard = (ClipboardManager)
                            getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText(CLIP_LABEL, mResult);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(MainActivity.this, R.string.copied_to_clipboard,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        mTxtResult = (TextView) findViewById(R.id.txt_result);

        startScanner();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SCAN_REQUEST_REQUEST) {
            if(resultCode == Activity.RESULT_OK){
                mResult = data.getStringExtra(ScanActivity.SCAN_RESULT);
                mTxtResult.setText(mResult);
            }
        }
    }

    private void startScanner() {
        Intent intent = new Intent(MainActivity.this, ScanActivity.class);
        startActivityForResult(intent, SCAN_REQUEST_REQUEST);
    }

}
