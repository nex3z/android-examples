package com.nex3z.examples.multicastsocketexample;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class MessageModel {
    public static final int RECV = 0;
    public static final int SEND = 1;
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({RECV, SEND})
    public @interface Direction {}

    @Direction private final int mDirection;
    private final String mAddress;
    private final int mPort;
    private final String mMessage;

    public MessageModel(@Direction int direction, String address, int port, String message) {
        mDirection = direction;
        mAddress = address;
        mPort = port;
        mMessage = message;
    }

    public int getDirection() {
        return mDirection;
    }

    public String getAddress() {
        return mAddress;
    }

    public int getPort() {
        return mPort;
    }

    public String getMessage() {
        return mMessage;
    }

    @Override
    public String toString() {
        return "MessageModel[" + mAddress + ":" + mPort + " " + mMessage + "]";
    }
}
