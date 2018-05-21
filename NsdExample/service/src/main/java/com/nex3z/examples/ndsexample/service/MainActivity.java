package com.nex3z.examples.ndsexample.service;

import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String SERVICE_TYPE = "_http._tcp.";
    private static final String SERVICE_NAME = "NsdDemo";
    private static final int PORT = 1234;

    private TextView mTvStatus;
    private NsdManager mNsdManager;
    private NsdServiceInfo mNsdServiceInfo;
    private NsdManager.RegistrationListener mRegistrationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTvStatus = (TextView) findViewById(R.id.tv_status);

        init();
        registerService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterService();
    }

    private void init() {
        mNsdServiceInfo  = new NsdServiceInfo();
        mNsdServiceInfo.setServiceName(SERVICE_NAME);
        mNsdServiceInfo.setServiceType(SERVICE_TYPE);
        mNsdServiceInfo.setPort(PORT);

        mNsdManager = (NsdManager) getSystemService(NSD_SERVICE);

        mRegistrationListener = new RegistrationListener();
    }

    private void registerService() {
        mNsdManager.registerService(
                mNsdServiceInfo, NsdManager.PROTOCOL_DNS_SD, mRegistrationListener);
    }

    private void unregisterService() {
        mNsdManager.unregisterService(mRegistrationListener);
    }


    private void showMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTvStatus.append(message + "\n");
            }
        });
    }

    private class RegistrationListener implements NsdManager.RegistrationListener {
        @Override
        public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
            Log.v(LOG_TAG, "onRegistrationFailed(): serviceInfo = " + serviceInfo
                    + ", errorCode = " + errorCode);
            showMessage("Registration Failed: serviceInfo = " + serviceInfo
                    + ", errorCode = " + errorCode);
        }

        @Override
        public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
            Log.v(LOG_TAG, "onUnregistrationFailed(): serviceInfo = " + serviceInfo
                    + ", errorCode = " + errorCode);
            showMessage("Unregistration Failed: serviceInfo = " + serviceInfo
                    + ", errorCode = " + errorCode);
        }

        @Override
        public void onServiceRegistered(NsdServiceInfo serviceInfo) {
            Log.v(LOG_TAG, "onServiceRegistered(): serviceInfo = " + serviceInfo);
            showMessage("Service Registered: serviceInfo = " + serviceInfo);
        }

        @Override
        public void onServiceUnregistered(NsdServiceInfo serviceInfo) {
            Log.v(LOG_TAG, "onServiceUnregistered(): serviceInfo = " + serviceInfo);
            showMessage("Service Unregistered: serviceInfo = " + serviceInfo);
        }
    }
}
