package com.nex3z.boundserviceexample.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nex3z.boundserviceexample.R;

public class AidlActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEditOp1;
    private EditText mEditOp2;
    private TextView mResult;
    IAidlService mService;
    private boolean mIsBound;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mService = IAidlService.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl);

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
        int result;

        try {
            switch (view.getId()) {
                case R.id.btn_add:
                    result = mService.add(op1, op2);
                    break;
                case R.id.btn_minus:
                    result = mService.minus(op1, op2);
                    break;
                case R.id.btn_multiply:
                    result = mService.multiply(op1, op2);
                    break;
                case R.id.btn_divide:
                    result = mService.divide(op1, op2);
                    break;
                default:
                    return;
            }
            mResult.setText(String.valueOf(result));
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (ArithmeticException e) {
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            mResult.setText(R.string.error);
        }
    }

    private void doBindService() {
        bindService(new Intent(this, AidlService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    private void doUnbindService() {
        if (mIsBound) {
            unbindService(mConnection);
            mIsBound = false;
        }
    }
}
