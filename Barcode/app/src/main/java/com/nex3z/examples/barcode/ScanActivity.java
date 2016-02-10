package com.nex3z.examples.barcode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private static final String LOG_TAG = ScanActivity.class.getSimpleName();

    static final String SCAN_RESULT = "scan_result";
    static final String SCAN_FORMAT = "scan_format";

    private ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mScannerView = new ZXingScannerView(this);

        setContentView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.scan_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_flash:
                if (mScannerView != null) {
                    boolean isFlashOn = mScannerView.getFlash();
                    if (isFlashOn) {
                        mScannerView.setFlash(false);
                        item.setIcon(R.drawable.ic_flash_on_white_24dp);
                    } else {
                        mScannerView.setFlash(true);
                        item.setIcon(R.drawable.ic_flash_off_white_24dp);
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void handleResult(Result result) {
        Log.v(LOG_TAG, result.getText() + ", " + result.getBarcodeFormat().toString());

        Toast.makeText(this, result.getText(), Toast.LENGTH_LONG).show();

        Intent returnIntent = new Intent();
        returnIntent.putExtra(SCAN_RESULT, result.toString());
        returnIntent.putExtra(SCAN_FORMAT, result.getBarcodeFormat().toString());
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}