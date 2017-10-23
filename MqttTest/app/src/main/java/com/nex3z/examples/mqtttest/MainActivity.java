package com.nex3z.examples.mqtttest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
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
    @BindView(R.id.btn_subscribe) Button mBtnSubscribe;
    @BindView(R.id.et_publish_topic) EditText mEtPublicTopic;
    @BindView(R.id.et_publish_qos) EditText mEtPublicQos;
    @BindView(R.id.et_publish_content) EditText mEtPublishContent;
    @BindView(R.id.btn_publish) Button mBtnPublish;
    @BindView(R.id.tv_received) TextView mTvReceived;

    private final MemoryPersistence mPersistence = new MemoryPersistence();
    private MqttClient mClient;

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
        if (TextUtils.isEmpty(topic) || mClient == null || !mClient.isConnected()) {
            return;
        }

        try {
            mClient.subscribe(topic);
            Log.i(LOG_TAG, "onSubscribeClick(): Subscribed " + topic);
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

        MqttMessage message = new MqttMessage(content.getBytes());
        message.setQos(Integer.parseInt(qos));
        try {
            mClient.publish(topic, message);
            Log.i(LOG_TAG, "onPublishClick(): Published " + topic + ", " + message);
        } catch (MqttException e) {
            Log.e(LOG_TAG, "onPublishClick():", e);
        }
    }

    private void initView() {
        mEtClientId.setText(UUID.randomUUID().toString());
        mEtBrokerAddress.setText("ws://iot.eclipse.org:80/ws");
        mEtSubscribeTopic.setText(R.string.default_topic);
        mEtPublicTopic.setText(R.string.default_topic);
        mEtPublicQos.setText(String.valueOf(DEFAULT_QOS));
        mEtPublishContent.setText(R.string.default_content);
    }

    private void connect() {
        String brokerAddress = mEtBrokerAddress.getText().toString();
        String clientId = mEtClientId.getText().toString();
        if (TextUtils.isEmpty(brokerAddress) || TextUtils.isEmpty(clientId)) {
            return;
        }

        try {
            mClient = new MqttClient(brokerAddress, clientId, mPersistence);
            mClient.setCallback(new MqttListener());

            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            Log.i(LOG_TAG, "connect(): Trying to connect " + brokerAddress);
            mClient.connect(options);
            Log.i(LOG_TAG, "connect(): Connected");

            mBtnConnect.setText(R.string.disconnect);
            mBtnPublish.setEnabled(true);
            mBtnSubscribe.setEnabled(true);
        } catch (MqttException e) {
            Log.e(LOG_TAG, "connect():", e);
        }
    }

    public void disconnect() {
        if (mClient != null && mClient.isConnected()) {
            try {
                mClient.disconnect();
                mClient = null;
                mBtnConnect.setText(R.string.connect);
                mBtnPublish.setEnabled(false);
                mBtnSubscribe.setEnabled(false);
            } catch (MqttException e) {
                Log.e(LOG_TAG, "disconnectInternal():", e);
            }
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
            mTvReceived.post(new Runnable() {
                @Override
                public void run() {
                    mTvReceived.append(message.getId() + ", " + topic + ", " + message);
                    mTvReceived.append("\n");
                }
            });
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            Log.v(LOG_TAG, "deliveryComplete(): id = " + token.getMessageId());
        }
    }
}
