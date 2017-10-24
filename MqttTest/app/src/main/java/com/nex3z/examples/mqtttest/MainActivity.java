package com.nex3z.examples.mqtttest;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int DEFAULT_QOS = 2;

    @BindView(R.id.et_client_id) EditText mEtClientId;
    @BindView(R.id.et_broker_address) EditText mEtBrokerAddress;
    @BindView(R.id.btn_connect) Button mBtnConnect;
    @BindView(R.id.et_subscribe_topic) EditText mEtSubscribeTopic;
    @BindView(R.id.et_subscribe_qos) EditText mEtSubscribeQos;
    @BindView(R.id.btn_subscribe) Button mBtnSubscribe;
    @BindView(R.id.et_publish_topic) EditText mEtPublicTopic;
    @BindView(R.id.et_publish_qos) EditText mEtPublicQos;
    @BindView(R.id.et_publish_content) EditText mEtPublishContent;
    @BindView(R.id.btn_publish) Button mBtnPublish;
    @BindView(R.id.sv_received) ScrollView mSvReceived;
    @BindView(R.id.tv_received) TextView mTvReceived;

    private final MemoryPersistence mPersistence = new MemoryPersistence();
    private MqttAsyncClient mClient;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    @OnClick(R.id.btn_connect)
    void onConnectClick() {
        if (mClient == null) {
            connect();
        } else {
            disconnect();
        }
    }

    @OnClick(R.id.btn_subscribe)
    void onSubscribeClick() {
        String topic = mEtSubscribeTopic.getText().toString();
        String qos = mEtSubscribeQos.getText().toString();
        if (TextUtils.isEmpty(topic) || TextUtils.isEmpty(qos) || !TextUtils.isDigitsOnly(qos)) {
            return;
        }

        Log.v(LOG_TAG, "onSubscribeClick(): Subscribing topic = " + topic + ", qos = " + qos);

        try {
            mClient.subscribe(topic, Integer.parseInt(qos), "Subscribe Context",
                    new ActionListener(R.string.msg_subscribe_success, R.string.msg_subscribe_failed));
        } catch (MqttException e) {
            Log.e(LOG_TAG, "onSubscribeClick():", e);
        }
    }

    @OnClick(R.id.btn_publish)
    void onPublishClick() {
        String topic = mEtPublicTopic.getText().toString();
        String content = mEtPublishContent.getText().toString();
        String qos = mEtPublicQos.getText().toString();
        if (TextUtils.isEmpty(topic) || TextUtils.isEmpty(content) || TextUtils.isEmpty(qos)
                || !TextUtils.isDigitsOnly(qos)) {
            return;
        }

        Log.v(LOG_TAG, "onPublishClick(): Publishing topic = " + topic + ", content = " + content
                + ", qos = " + qos);

        MqttMessage message = new MqttMessage(content.getBytes());
        message.setQos(Integer.parseInt(qos));
        try {
            mClient.publish(topic, message, "Publish Context",
                    new ActionListener(R.string.msg_publish_success, R.string.msg_publish_failed));
        } catch (MqttException e) {
            Log.e(LOG_TAG, "onPublishClick():", e);
        }
    }

    private void initView() {
        mEtClientId.setText(UUID.randomUUID().toString());
        mEtBrokerAddress.setText("ws://iot.eclipse.org:80/ws");
        mEtSubscribeTopic.setText(R.string.caption_default_topic);
        mEtSubscribeQos.setText(String.valueOf(DEFAULT_QOS));
        mEtPublicTopic.setText(R.string.caption_default_topic);
        mEtPublicQos.setText(String.valueOf(DEFAULT_QOS));
        mEtPublishContent.setText(R.string.caption_default_content);
    }

    private void connect() {
        String brokerAddress = mEtBrokerAddress.getText().toString();
        String clientId = mEtClientId.getText().toString();
        if (TextUtils.isEmpty(brokerAddress) || TextUtils.isEmpty(clientId)) {
            return;
        }

        Log.v(LOG_TAG, "connect(): Connecting brokerAddress = " + brokerAddress
                + ", clientId = " + clientId);

        try {
            mClient = new MqttAsyncClient(brokerAddress, clientId, mPersistence);
            mClient.setCallback(new MqttListener());

            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setConnectionTimeout(10);
            mClient.connect(options, "Connect Context", new ConnectListener());
        } catch (MqttException e) {
            Log.e(LOG_TAG, "connect():", e);
        }
    }

    public void disconnect() {
        if (mClient != null && mClient.isConnected()) {
            Log.v(LOG_TAG, "disconnect(): Disconnecting");
            try {
                mClient.disconnect("Disconnect Context", new DisconnectListener());
            } catch (MqttException e) {
                Log.e(LOG_TAG, "disconnectInternal():", e);
            }
        }
    }

    private class ConnectListener implements IMqttActionListener {
        @Override
        public void onSuccess(IMqttToken asyncActionToken) {
            Log.v(LOG_TAG, "ConnectListener: onSuccess()");
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, R.string.msg_connect_success, Toast.LENGTH_SHORT).show();
                    mBtnConnect.setText(R.string.caption_disconnect);
                    mBtnPublish.setEnabled(true);
                    mBtnSubscribe.setEnabled(true);
                }
            });
        }

        @Override
        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            Log.e(LOG_TAG, "ConnectListener: onFailure()", exception);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, R.string.msg_connect_failed, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private class DisconnectListener implements IMqttActionListener {
        @Override
        public void onSuccess(IMqttToken asyncActionToken) {
            Log.v(LOG_TAG, "DisconnectListener: onSuccess()");
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, R.string.msg_disconnect_success, Toast.LENGTH_SHORT).show();
                    mClient = null;
                    mBtnConnect.setText(R.string.caption_connect);
                    mBtnPublish.setEnabled(false);
                    mBtnSubscribe.setEnabled(false);
                }
            });
        }

        @Override
        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            Log.e(LOG_TAG, "DisconnectListener: onFailure()", exception);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, R.string.msg_disconnect_failed, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private class ActionListener implements IMqttActionListener {
        private final int mSuccessMsgId;
        private final int mFaildMsgId;

        ActionListener(int successMsgId, int failMsgId) {
            mSuccessMsgId = successMsgId;
            mFaildMsgId = failMsgId;
        }

        @Override
        public void onSuccess(IMqttToken asyncActionToken) {
            Log.v(LOG_TAG, "ActionListener: onSuccess()");
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, mSuccessMsgId, Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            Log.e(LOG_TAG, "ActionListener: onFailure()", exception);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, mFaildMsgId, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private class MqttListener implements MqttCallback {
        @Override
        public void connectionLost(Throwable cause) {
            Log.w(LOG_TAG, "connectionLost(): cause = ", cause);
        }

        @Override
        public void messageArrived(final String topic, final MqttMessage message) throws Exception {
            Log.v(LOG_TAG, "messageArrived(): topic = " + topic + ", message = " + message
                    + ", id = " + message.getId());
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mTvReceived.append(message.getId() + ", " + topic + ", " + message);
                    mTvReceived.append("\n");
                    mSvReceived.scrollTo(0, mSvReceived.getBottom());
                }
            });
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            Log.v(LOG_TAG, "deliveryComplete(): id = " + token.getMessageId());
        }
    }
}
