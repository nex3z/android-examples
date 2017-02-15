package com.nex3z.examples.multicastsocketexample;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String ADDR = "225.0.0.3";
    private static final int PORT = 8888;
    private static final int BUF_SIZE = 256;
    private static final String ACK = "ACK ";
    private static final int READ_TIMEOUT = 2000;

    private WifiManager.MulticastLock mMulticastLock;
    private TextView mTvResult;
    private MulticastTask mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvResult = (TextView) findViewById(R.id.tv_message);
        Button button = (Button) findViewById(R.id.btn_send);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTvResult.setText("");
                String sendMsg = "Hello World";
                mTask = new MulticastTask();
                mTask.execute(sendMsg);
            }
        });

        init();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mTask != null && mTask.getStatus() != AsyncTask.Status.FINISHED) {
            mTask.cancel(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMulticastLock != null) {
            mMulticastLock.release();
        }
    }

    private void init() {
        WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        mMulticastLock = wifiManager.createMulticastLock("multicast_test");
        mMulticastLock.acquire();
    }

    private class MulticastTask extends AsyncTask<String, Void, String[]> {
        @Override
        protected String[] doInBackground(String... params) {
            String sendMsg = params[0];
            try {
                MulticastSocket socket = new MulticastSocket(PORT);
                socket.setSoTimeout(READ_TIMEOUT);
                InetAddress address = InetAddress.getByName(ADDR);
                socket.joinGroup(address);

                DatagramPacket sendPacket = new DatagramPacket(sendMsg.getBytes(),
                        sendMsg.getBytes().length, InetAddress.getByName(ADDR), PORT);
                Log.v(LOG_TAG, "MulticastTask: Sending message " + sendMsg);
                socket.send(sendPacket);

                byte[] buf = new byte[BUF_SIZE];
                while (!isCancelled()) {
                    DatagramPacket recvPacket = new DatagramPacket(buf, buf.length);
                    try {
                        socket.receive(recvPacket);
                    } catch (SocketTimeoutException e) {
                        Log.w(LOG_TAG, "MulticastTask: Timeout");
                        return new String[] {"Timeout", "Unknown"};
                    }
                    String msg = new String(recvPacket.getData(), 0, recvPacket.getLength());
                    Log.v(LOG_TAG, "MulticastTask: Received msg: " + msg);
                    if (msg.startsWith(ACK)) {
                        Log.v(LOG_TAG, "MulticastTask: ACK received. Task finished.");
                        String remoteAddr = recvPacket.getAddress().getHostAddress();
                        int remotePort = recvPacket.getPort();
                        return new String[] {msg, remoteAddr + ":" + remotePort};
                    }
                }
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] result) {
            if (result != null) {
                mTvResult.setText(String.format(getString(R.string.result), result[0], result[1]));
            } else {
                Log.e(LOG_TAG, "MulticastTask: Receive failed");
            }
        }
    }
}
