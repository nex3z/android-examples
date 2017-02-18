package com.nex3z.examples.multicastsocketexample;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.WorkerThread;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String DEFAULT_ADDRESS = "225.0.0.3";
    private static final int DEFAULT_PORT = 8888;
    private static final int BUFFER_SIZE = 256;
    private static final String MULTICAST_TAG = "multicast_recv";
    private static final int READ_TIMEOUT = 2000;

    private RecyclerView mRvList;
    private EditText mEtSend;
    private MessageAdapter mAdapter;
    private List<MessageModel> mMessages = new ArrayList<>();
    private String mAddress = DEFAULT_ADDRESS;
    private int mPort = DEFAULT_PORT;
    private WifiManager.MulticastLock mMulticastLock;
    private MulticastTask mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initViews();
        initMulticast();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_clear) {
            mMessages.clear();
            mAdapter.notifyDataSetChanged();
            return true;
        }
        return super.onOptionsItemSelected(item);
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

    private void initViews() {
        mEtSend = (EditText) findViewById(R.id.et_input);

        ImageButton btnSend = (ImageButton) findViewById(R.id.btn_send);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = mEtSend.getText().toString();
                SendService.startActionSend(MainActivity.this,
                        mAddress, mPort, msg);
                mMessages.add(new MessageModel(MessageModel.SEND, mAddress, mPort, msg));
                mAdapter.notifyItemInserted(mMessages.size() - 1);
                mRvList.scrollToPosition(mMessages.size() - 1);
            }
        });

        mRvList = (RecyclerView) findViewById(R.id.rv_messages);
        mAdapter = new MessageAdapter(this);
        mRvList.setAdapter(mAdapter);
        mRvList.setLayoutManager(new LinearLayoutManager(this));
        mRvList.setItemAnimator(null);
        mRvList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        mAdapter.setMessageCollection(mMessages);
    }

    private void initMulticast() {
        WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        mMulticastLock = wifiManager.createMulticastLock(MULTICAST_TAG);
        mMulticastLock.acquire();

        mTask = new MulticastTask();
        mTask.execute(mAddress, String.valueOf(mPort));
    }

    private class MulticastTask extends AsyncTask<String, MessageModel, Void> {
        @Override
        protected void onProgressUpdate(MessageModel... values) {
            mMessages.add(values[0]);
            mAdapter.notifyItemInserted(mMessages.size() - 1);
            mRvList.scrollToPosition(mMessages.size() - 1);
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

                byte[] buf = new byte[BUFFER_SIZE];
                while (!isCancelled()) {
                    DatagramPacket recvPacket = new DatagramPacket(buf, buf.length);
                    try {
                        socket.receive(recvPacket);
                    } catch (SocketTimeoutException e) {
                        continue;
                    }
                    publishMessage(MessageModel.RECV, recvPacket);
                }
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @WorkerThread
        private void publishMessage(@MessageModel.Direction int direction, DatagramPacket packet) {
            MessageModel messageModel = new MessageModel(
                    direction,
                    packet.getAddress().getHostAddress(),
                    packet.getPort(),
                    new String(packet.getData(), 0, packet.getLength()));
            publishProgress(messageModel);
        }
    }
}
