package com.nex3z.examples.barcode;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final int SCAN_REQUEST_ZXING_SCANNER = 1;
    private static final int SCAN_REQUEST_ZXING_ANDROID_EMBEDDED = 2;
    private static final String CLIP_LABEL = "scan_result";

    private TextView mTxtResult;
    private String mResult = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnZXingScanner = (Button) findViewById(R.id.btn_zxing_scanner_view);
        btnZXingScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ZXingScannerActivity.class);
                startActivityForResult(intent, SCAN_REQUEST_ZXING_SCANNER);
            }
        });

        Button btnZXingAndroidEmbedded = (Button) findViewById(R.id.btn_zxing_android_embedded);
        btnZXingAndroidEmbedded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.setBeepEnabled(false);
                integrator.initiateScan();
            }
        });

        Button mBtnCopy = (Button) findViewById(R.id.btn_copy);
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v(LOG_TAG, "onActivityResult(): requestCode = " + requestCode);
        if (requestCode == SCAN_REQUEST_ZXING_SCANNER) {
            if(resultCode == Activity.RESULT_OK){
                mResult = data.getStringExtra(ZXingScannerActivity.SCAN_RESULT);
                mTxtResult.setText(mResult);
            }
        } else {
            IntentResult result = IntentIntegrator.parseActivityResult(
                    requestCode, resultCode, data);
            if(result != null && result.getContents() != null) {
                mResult = result.getContents();
                mTxtResult.setText(mResult);
            }
        }
    }

}
