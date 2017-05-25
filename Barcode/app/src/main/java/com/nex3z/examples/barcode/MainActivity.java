package com.nex3z.examples.barcode;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.ResultPoint;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private static final int SCAN_REQUEST_ZXING_SCANNER = 1;
    private static final int SCAN_REQUEST_ZXING_ANDROID_EMBEDDED = 2;
    private static final String CLIP_LABEL = "scan_result";

    private TextView mTxtResult;
    DecoratedBarcodeView mDbvScanner;
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

        mDbvScanner = (DecoratedBarcodeView) findViewById(R.id.dbv_scanner);
        mDbvScanner.setStatusText("");
        mDbvScanner.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                mResult = result.getText();
                mTxtResult.setText(mResult);
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {}
        });

        mTxtResult = (TextView) findViewById(R.id.txt_result);

        requestPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDbvScanner.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDbvScanner.pause();
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

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            Log.v(LOG_TAG, "requestPermission(): Requesting.");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            Log.v(LOG_TAG, "requestPermission(): Permission was granted already.");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v(LOG_TAG, "onRequestPermissionsResult(): Permission granted");
                } else {
                    Toast.makeText(this, R.string.need_camera_permission, Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
