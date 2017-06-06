package com.nex3z.examples.bluetoothdiscovery;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    private BluetoothSocket mSocket;

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

        boolean permissionGranted = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionGranted = checkPermission();
        }

        Log.v(LOG_TAG, "startDiscovery(): permissionGranted = " + permissionGranted);
        if (permissionGranted) {
            stopDiscovery();
            boolean success = mBluetoothAdapter.startDiscovery();
            Log.v(LOG_TAG, "startDiscovery(): success = " + success);
        }
    }

    private void stopDiscovery() {
        if (mBluetoothAdapter != null && mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
    }

    private void connect(BluetoothDevice device) {
        stopDiscovery();
        disconnect();
        Toast.makeText(this, "Connecting..." + device.getName() + " " + device.getAddress(),
                Toast.LENGTH_SHORT).show();
        new ConnectTask(this).execute(device);
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

    private void disconnect() {
        if (mSocket != null && mSocket.isConnected()) {
            try {
                mSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mSocket = null;
    }

    private void onConnected(BluetoothSocket socket) {
        if (socket == null) {
            Toast.makeText(this, "Connect failed.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Connected.", Toast.LENGTH_SHORT).show();
        }
        mSocket = socket;
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

    private static class ConnectTask extends AsyncTask<BluetoothDevice, Void, BluetoothSocket> {
        private final UUID BLUETOOTH_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        private WeakReference<MainActivity> mRef;

        ConnectTask(MainActivity activity) {
            mRef = new WeakReference<>(activity);
        }

        @Override
        protected BluetoothSocket doInBackground(BluetoothDevice... bluetoothDevices) {
            if (bluetoothDevices.length == 0) {
                return null;
            }

            BluetoothDevice device = bluetoothDevices[0];
            BluetoothSocket socket = null;

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                socket = device.createRfcommSocketToServiceRecord(BLUETOOTH_UUID);
            } catch (IOException exception) {
                Log.e(LOG_TAG, "doInBackground(): Socket's create() method failed",
                        exception);
                return null;
            }

            if (socket != null) {
                Log.v(LOG_TAG, "doInBackground(): Trying to connect socket " + socket);
                try {
                    socket.connect();
                    return socket;
                } catch (IOException connectException) {
                    Log.e(LOG_TAG, "doInBackground(): Connect failed", connectException);
                    try {
                        socket.close();
                    } catch (IOException closeException) {
                        Log.e(LOG_TAG, "doInBackground(): Could not close the client socket",
                                closeException);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(BluetoothSocket socket) {
            MainActivity activity = mRef.get();
            if (activity != null) {
                activity.onConnected(socket);
            }
        }
    }
}
