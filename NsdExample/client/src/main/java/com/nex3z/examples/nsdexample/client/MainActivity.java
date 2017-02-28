package com.nex3z.examples.nsdexample.client;

import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String SERVICE_TYPE = "_http._tcp.";
    private static final String SERVICE_NAME = "NdsDemo";

    private TextView mTvStatus;
    private NsdManager mNsdManager;
    private NsdManager.DiscoveryListener mDiscoveryListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTvStatus = (TextView) findViewById(R.id.tv_status);

        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        startDiscovery();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopDiscovery();
    }

    private void init() {
        mNsdManager = (NsdManager) getSystemService(NSD_SERVICE);

        mDiscoveryListener = new NsdManager.DiscoveryListener() {
            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                Log.v(LOG_TAG, "onStartDiscoveryFailed(): serviceType = " + serviceType
                        + ", errorCode = " + errorCode);
                showMessage("onStartDiscoveryFailed(): serviceType = " + serviceType
                        + ", errorCode = " + errorCode);
            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                Log.v(LOG_TAG, "onStopDiscoveryFailed(): serviceType = " + serviceType
                        + ", errorCode = " + errorCode);
                showMessage("onStopDiscoveryFailed(): serviceType = " + serviceType
                        + ", errorCode = " + errorCode);
            }

            @Override
            public void onDiscoveryStarted(String serviceType) {
                Log.v(LOG_TAG, "onDiscoveryStarted(): serviceType = " + serviceType);
                showMessage("onDiscoveryStarted(): serviceType = " + serviceType);
            }

            @Override
            public void onDiscoveryStopped(String serviceType) {
                Log.v(LOG_TAG, "onDiscoveryStopped(): serviceType = " + serviceType);
                showMessage("onDiscoveryStopped(): serviceType = " + serviceType);
            }

            @Override
            public void onServiceFound(NsdServiceInfo serviceInfo) {
                Log.v(LOG_TAG, "onServiceFound(): serviceInfo = " + serviceInfo);
                showMessage("Service Found: serviceInfo = " + serviceInfo);

                if (serviceInfo.getServiceType().startsWith(SERVICE_TYPE)
                        && serviceInfo.getServiceName().startsWith(SERVICE_NAME)) {
                    Log.v(LOG_TAG, "onServiceFound(): Service found.");
                    showMessage("Service found!");

                    mNsdManager.resolveService(serviceInfo, new NsdManager.ResolveListener() {
                        @Override
                        public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                            Log.v(LOG_TAG, "onResolveFailed(): serviceInfo = " + serviceInfo
                                    + ", errorCode = " + errorCode);
                            showMessage("Resolve Failed: serviceInfo = " + serviceInfo
                                    + ", errorCode = " + errorCode);
                        }

                        @Override
                        public void onServiceResolved(NsdServiceInfo serviceInfo) {
                            Log.v(LOG_TAG, "onServiceResolved(): serviceInfo = " + serviceInfo);
                            showMessage("Service Resolved: serviceInfo = " + serviceInfo);
                        }
                    });
                }
            }

            @Override
            public void onServiceLost(NsdServiceInfo serviceInfo) {
                Log.v(LOG_TAG, "Service Lost: serviceInfo = " + serviceInfo);
            }
        };
    }

    private void startDiscovery() {
        mNsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);
    }

    private void stopDiscovery() {
        mNsdManager.stopServiceDiscovery(mDiscoveryListener);
    }

    private void showMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTvStatus.append(message + "\n");
            }
        });
    }
}
