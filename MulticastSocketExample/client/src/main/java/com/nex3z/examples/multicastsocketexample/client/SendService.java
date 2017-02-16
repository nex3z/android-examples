package com.nex3z.examples.multicastsocketexample.client;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class SendService extends IntentService {
    private static final String LOG_TAG = SendService.class.getSimpleName();

    private static final String ACTION_SEND = "com.nex3z.examples.multicastsocketexample.action.ACTION_SEND";
    private static final String EXTRA_ADDRESS = "com.nex3z.examples.multicastsocketexample.extra.ADDRESS";
    private static final String EXTRA_PORT = "com.nex3z.examples.multicastsocketexample.extra.PORT";
    private static final String EXTRA_MESSAGE = "com.nex3z.examples.multicastsocketexample.extra.MESSAGE";
    private static final String MULTICAST_TAG = "multicast_intentservice";

    public SendService() {
        super("SendService");
    }

    public static void startActionSend(Context context, String address, int port, String message) {
        Intent intent = new Intent(context, SendService.class);
        intent.setAction(ACTION_SEND);
        intent.putExtra(EXTRA_ADDRESS, address);
        intent.putExtra(EXTRA_PORT, port);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SEND.equals(action)) {
                final String address = intent.getStringExtra(EXTRA_ADDRESS);
                final int port = intent.getIntExtra(EXTRA_PORT, 8888);
                final String message = intent.getStringExtra(EXTRA_MESSAGE);
                handleActionSend(address, port, message);
            }
        }
    }

    private void handleActionSend(String multicastAddress, int port, String message) {
        WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        WifiManager.MulticastLock multicastLock = wifiManager.createMulticastLock(MULTICAST_TAG);
        multicastLock.acquire();

        try {
            MulticastSocket socket = new MulticastSocket(port);
            InetAddress address = InetAddress.getByName(multicastAddress);
            socket.joinGroup(address);

            DatagramPacket sendPacket = new DatagramPacket(
                    message.getBytes(), message.getBytes().length, address, port);
            Log.v(LOG_TAG, "Sending message " + message);
            socket.send(sendPacket);

            socket.leaveGroup(address);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            multicastLock.release();
        }
    }

}
