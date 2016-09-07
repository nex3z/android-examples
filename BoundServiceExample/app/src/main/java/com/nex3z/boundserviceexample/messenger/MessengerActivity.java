package com.nex3z.boundserviceexample.messenger;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nex3z.boundserviceexample.R;

public class MessengerActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEditOp1;
    private EditText mEditOp2;
    private TextView mResult;
    private Messenger mService = null;
    private boolean mIsBound;

    private final Messenger mMessenger = new Messenger(new ResultHandler());

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mService = new Messenger(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messager);

        mEditOp1 = (EditText) findViewById(R.id.edit_op1);
        mEditOp2 = (EditText) findViewById(R.id.edit_op2);
        mResult = (TextView) findViewById(R.id.tv_result);

        Button btnAdd = (Button) findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(this);
        Button btnMinus = (Button) findViewById(R.id.btn_minus);
        btnMinus.setOnClickListener(this);
        Button btnMultiply = (Button) findViewById(R.id.btn_multiply);
        btnMultiply.setOnClickListener(this);
        Button btnDivide = (Button) findViewById(R.id.btn_divide);
        btnDivide.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        doBindService();
    }

    @Override
    protected void onStop() {
        super.onStop();
        doUnbindService();
    }

    @Override
    public void onClick(View view) {
        if (mEditOp1.getText().toString().isEmpty() || mEditOp2.getText().toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.empty_input_error), Toast.LENGTH_SHORT).show();
            return;
        }

        int op1 = Integer.valueOf(mEditOp1.getText().toString());
        int op2 = Integer.valueOf(mEditOp2.getText().toString());
        Message msg = null;

        switch (view.getId()) {
            case R.id.btn_add:
                msg = Message.obtain(null, MessengerService.MSG_ADD, op1, op2);
                break;
            case R.id.btn_minus:
                msg = Message.obtain(null, MessengerService.MSG_MINUS, op1, op2);
                break;
            case R.id.btn_multiply:
                msg = Message.obtain(null, MessengerService.MSG_MULTIPLY, op1, op2);
                break;
            case R.id.btn_divide:
                msg = Message.obtain(null, MessengerService.MSG_DIVIDE, op1, op2);
                break;
            default:
                return;
        }

        if (msg != null) {
            msg.replyTo = mMessenger;
            try {
                mService.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private class ResultHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MessengerService.MSG_RESULT:
                    mResult.setText(String.valueOf(msg.arg1));
                    break;
                case MessengerService.MSG_ERROR:
                    mResult.setText(R.string.error);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private void doBindService() {
        bindService(new Intent(this, MessengerService.class), mConnection,
                Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    private void doUnbindService() {
        if (mIsBound) {
            unbindService(mConnection);
            mIsBound = false;
        }
    }

}
