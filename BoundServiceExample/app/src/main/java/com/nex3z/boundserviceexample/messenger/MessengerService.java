package com.nex3z.boundserviceexample.messenger;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class MessengerService extends Service {
    private static final String LOG_TAG = MessengerService.class.getSimpleName();

    public static final int MSG_ADD = 1;
    public static final int MSG_MINUS = 2;
    public static final int MSG_MULTIPLY = 3;
    public static final int MSG_DIVIDE = 4;
    public static final int MSG_RESULT = 4;
    public static final int MSG_ERROR = 5;

    final Messenger mMessenger = new Messenger(new MathHandler());

    public MessengerService() {}

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(LOG_TAG, "onDestroy()");
    }

    private class MathHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            int result;
            switch (msg.what) {
                case MSG_ADD:
                    result = msg.arg1 + msg.arg2;
                    sendResult(result, msg.replyTo);
                    break;
                case MSG_MINUS:
                    result = msg.arg1 - msg.arg2;
                    sendResult(result, msg.replyTo);
                    break;
                case MSG_MULTIPLY:
                    result = msg.arg1 * msg.arg2;
                    sendResult(result, msg.replyTo);
                    break;
                case MSG_DIVIDE:
                    if (msg.arg2 == 0) {
                        sendError(msg.replyTo);
                    } else {
                        result = msg.arg1 / msg.arg2;
                        sendResult(result, msg.replyTo);
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private void sendResult(int result, Messenger replyTo) {
        try {
            replyTo.send(Message.obtain(null, MSG_RESULT, result, 0));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    private void sendError(Messenger replyTo) {
        try {
            replyTo.send(Message.obtain(null, MSG_ERROR, 0, 0));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
