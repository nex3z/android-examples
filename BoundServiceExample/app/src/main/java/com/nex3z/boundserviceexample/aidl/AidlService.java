package com.nex3z.boundserviceexample.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class AidlService extends Service {
    private static final String LOG_TAG = AidlService.class.getSimpleName();

    public AidlService() {}

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(LOG_TAG, "onDestroy()");
    }

    private final IAidlService.Stub mBinder = new IAidlService.Stub() {
        @Override
        public int add(int op1, int op2) throws RemoteException {
            return op1 + op2;
        }

        @Override
        public int minus(int op1, int op2) throws RemoteException {
            return op1 - op2;
        }

        @Override
        public int multiply(int op1, int op2) throws RemoteException {
            return op1 * op2;
        }

        @Override
        public int divide(int op1, int op2) throws RemoteException {
            return op1 / op2;
        }
    };

}
