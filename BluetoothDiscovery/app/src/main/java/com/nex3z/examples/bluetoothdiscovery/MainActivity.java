package com.nex3z.examples.bluetoothdiscovery;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final int REQUEST_ENABLE_BT = 1001;
    private static final int REQUEST_PERMISSION = 2001;

    private RecyclerView mRvPaired;
    private RecyclerView mRvDiscovered;
    private Button mBtnDiscover;

    private final List<BluetoothDevice> mPairedDevices = new ArrayList<>();
    private BtDeviceAdapter mPairedAdapter;
    private final List<BluetoothDevice> mDiscoveredDevices = new ArrayList<>();
    private BtDeviceAdapter mDiscoveredAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private DiscoveryReceiver mReceiver = new DiscoveryReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnDiscover = (Button) findViewById(R.id.btn_discover);
        mBtnDiscover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discover();
            }
        });

        mRvPaired = (RecyclerView) findViewById(R.id.rv_paired);
        mRvDiscovered = (RecyclerView) findViewById(R.id.rv_discovered);

        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_OK) {
            Log.v(LOG_TAG, "onActivityResult(): Bluetooth is enabled");
            discover();
        } else if (requestCode == REQUEST_PERMISSION && resultCode == RESULT_OK) {
            Log.v(LOG_TAG, "onActivityResult(): Permission enabled");
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void init() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            mBtnDiscover.setEnabled(false);
            return;
        }

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        registerReceiver(mReceiver, filter);

        mPairedAdapter = new BtDeviceAdapter();
        mPairedAdapter.setDevices(mPairedDevices);
        mPairedAdapter.setOnBtDeviceClickListener(new BtDeviceAdapter.OnBtDeviceClickListener() {
            @Override
            public void onBtDeviceClick(int position) {
                connect(mPairedDevices.get(position));
            }
        });
        mRvPaired.setAdapter(mPairedAdapter);
        mRvPaired.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mDiscoveredAdapter = new BtDeviceAdapter();
        mDiscoveredAdapter.setDevices(mDiscoveredDevices);
        mDiscoveredAdapter.setOnBtDeviceClickListener(new BtDeviceAdapter.OnBtDeviceClickListener() {
            @Override
            public void onBtDeviceClick(int position) {
                connect(mDiscoveredDevices.get(position));
            }
        });
        mRvDiscovered.setAdapter(mDiscoveredAdapter);
        mRvDiscovered.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void discover() {
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            return;
        }
        queryPairedDevices();
        startDiscovery();
    }

    private void queryPairedDevices() {
        mPairedDevices.clear();
        mPairedDevices.addAll(mBluetoothAdapter.getBondedDevices());
        mPairedAdapter.notifyDataSetChanged();
        Log.v(LOG_TAG, "queryPairedDevices(): mPairedDevices = " + mPairedDevices);
    }

    private void startDiscovery() {
        mDiscoveredDevices.clear();
        mDiscoveredAdapter.notifyDataSetChanged();

        boolean permissionGranted = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionGranted = checkPermission();
        }

        Log.v(LOG_TAG, "startDiscovery(): permissionGranted = " + permissionGranted);
        if (permissionGranted) {
            if (mBluetoothAdapter.isDiscovering()) {
                mBluetoothAdapter.cancelDiscovery();
            }
            boolean success = mBluetoothAdapter.startDiscovery();
            Log.v(LOG_TAG, "startDiscovery(): success = " + success);
        }
    }

    private void connect(BluetoothDevice device) {

    }

    @TargetApi(23)
    private boolean checkPermission() {
        int accessCoarseLocation = checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int accessFineLocation = checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION);

        List<String> listRequestPermission = new ArrayList<String>();

        if (accessCoarseLocation != PackageManager.PERMISSION_GRANTED) {
            listRequestPermission.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (accessFineLocation != PackageManager.PERMISSION_GRANTED) {
            listRequestPermission.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (!listRequestPermission.isEmpty()) {
            String[] strRequestPermission = listRequestPermission.toArray(new String[listRequestPermission.size()]);
            requestPermissions(strRequestPermission, REQUEST_PERMISSION);
            return false;
        } else {
            return true;
        }
    }

    private class DiscoveryReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.v(LOG_TAG, "onReceive(): Found " + device);

                mDiscoveredDevices.add(device);
                mDiscoveredAdapter.notifyItemInserted(mDiscoveredDevices.size() - 1);
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                Log.v(LOG_TAG, "onReceive(): ACTION_DISCOVERY_STARTED");
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Log.v(LOG_TAG, "onReceive(): ACTION_DISCOVERY_FINISHED");
            }
        }
    }
}
