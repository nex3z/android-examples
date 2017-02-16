package com.nex3z.examples.multicastsocketexample.service;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String ADDRESS = "225.0.0.3";
    private static final int PORT = 8888;
    private static final int BUF_SIZE = 256;
    private static final String ACK = "ACK";
    private static final int READ_TIMEOUT = 2000;

    private RecyclerView mRvMessages;
    private MessageAdapter mAdapter;
    private List<MessageModel> mMessages = new ArrayList<>();
    private WifiManager.MulticastLock mMulticastLock;
    private MulticastTask mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRvMessages = (RecyclerView) findViewById(R.id.rv_messages);

        Button btnClear = (Button) findViewById(R.id.btn_clear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMessages.clear();
                mAdapter.notifyDataSetChanged();
            }
        });

        initRecyclerView();
        initMulticast();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMulticastLock != null) {
            mMulticastLock.release();
        }
        if (mTask != null && mTask.getStatus() != AsyncTask.Status.FINISHED) {
            mTask.cancel(true);
        }
    }

    private void initRecyclerView() {
        mAdapter = new MessageAdapter(this);
        mRvMessages.setAdapter(mAdapter);
        mRvMessages.setLayoutManager(new LinearLayoutManager(this));
        mRvMessages.setItemAnimator(null);
        mRvMessages.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mAdapter.setMessageCollection(mMessages);
    }

    private void initMulticast() {
        WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        mMulticastLock = wifiManager.createMulticastLock("multicast_test");
        mMulticastLock.acquire();

        mTask = new MulticastTask();
        mTask.execute(ADDRESS, String.valueOf(PORT));
    }

    private class MulticastTask extends AsyncTask<String, MessageModel, Void> {
        @Override
        protected void onProgressUpdate(MessageModel... values) {
            mMessages.add(values[0]);
            mAdapter.notifyItemInserted(mMessages.size() - 1);
            mRvMessages.scrollToPosition(mMessages.size() - 1);
        }

        @Override
        protected Void doInBackground(String... params) {
            String multicastAddr = params[0];
            int port = Integer.valueOf(params[1]);

            try {
                MulticastSocket socket = new MulticastSocket(port);
                socket.setSoTimeout(READ_TIMEOUT);
                InetAddress address = InetAddress.getByName(multicastAddr);
                socket.joinGroup(address);

                byte[] buf = new byte[BUF_SIZE];
                while (!isCancelled()) {
                    DatagramPacket recvPacket = new DatagramPacket(buf, buf.length);
                    try {
                        socket.receive(recvPacket);
                    } catch (SocketTimeoutException e) {
                        Log.w(LOG_TAG, "MulticastTask: Timeout");
                        continue;
                    }

                    String msg = new String(recvPacket.getData(), 0, recvPacket.getLength());

                    String remoteAddr = recvPacket.getAddress().getHostAddress();
                    int remotePort = recvPacket.getPort();
                    MessageModel message = new MessageModel(remoteAddr, remotePort, msg);
                    publishProgress(message);

                    Log.v(LOG_TAG, "MulticastTask: Received msg: " + msg);
                    if (!msg.startsWith(ACK)) {
                        String send = ACK + " " + msg;
                        DatagramPacket sendPacket = new DatagramPacket(
                                send.getBytes(), send.getBytes().length, address, PORT);
                        socket.send(sendPacket);
                        Log.v(LOG_TAG, "MulticastTask: Send ACK");
                    }
                }
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

}
