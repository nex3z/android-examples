package com.nex3z.examples.multicastsocketexample.service;

public class MessageModel {
    private final String mAddress;
    private final int mPort;
    private final String mMessage;

    public MessageModel(String address, int port, String message) {
        mAddress = address;
        mPort = port;
        mMessage = message;
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
