package com.nex3z.examples.eventbusexample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

public class NumberPadFragment extends Fragment implements View.OnClickListener {

    private GridLayout mGridView;

    public NumberPadFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_number_pad, container, false);
        mGridView = (GridLayout) rootView.findViewById(R.id.pad);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mGridView.findViewById(R.id.btn_clear).setOnClickListener(this);
        mGridView.findViewById(R.id.btn_divide).setOnClickListener(this);
        mGridView.findViewById(R.id.btn_multiply).setOnClickListener(this);
        mGridView.findViewById(R.id.btn_minus).setOnClickListener(this);
        mGridView.findViewById(R.id.btn_plus).setOnClickListener(this);
        mGridView.findViewById(R.id.btn_equal).setOnClickListener(this);
        mGridView.findViewById(R.id.btn_dot).setOnClickListener(this);
        mGridView.findViewById(R.id.btn_0).setOnClickListener(this);
        mGridView.findViewById(R.id.btn_1).setOnClickListener(this);
        mGridView.findViewById(R.id.btn_2).setOnClickListener(this);
        mGridView.findViewById(R.id.btn_3).setOnClickListener(this);
        mGridView.findViewById(R.id.btn_4).setOnClickListener(this);
        mGridView.findViewById(R.id.btn_5).setOnClickListener(this);
        mGridView.findViewById(R.id.btn_6).setOnClickListener(this);
        mGridView.findViewById(R.id.btn_7).setOnClickListener(this);
        mGridView.findViewById(R.id.btn_8).setOnClickListener(this);
        mGridView.findViewById(R.id.btn_9).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_clear:
                postEvent("C");
                break;
            case R.id.btn_divide:
                postEvent("/");
                break;
            case R.id.btn_multiply:
                postEvent("*");
                break;
            case R.id.btn_minus:
                postEvent("-");
                break;
            case R.id.btn_plus:
                postEvent("+");
                break;
            case R.id.btn_equal:
                postEvent("=");
                break;
            case R.id.btn_dot:
                postEvent(".");
                break;
            case R.id.btn_0:
                postEvent("0");
                break;
            case R.id.btn_1:
                postEvent("1");
                break;
            case R.id.btn_2:
                postEvent("2");
                break;
            case R.id.btn_3:
                postEvent("3");
                break;
            case R.id.btn_4:
                postEvent("4");
                break;
            case R.id.btn_5:
                postEvent("5");
                break;
            case R.id.btn_6:
                postEvent("6");
                break;
            case R.id.btn_7:
                postEvent("7");
                break;
            case R.id.btn_8:
                postEvent("8");
                break;
            case R.id.btn_9:
                postEvent("9");
                break;
            default:
                break;
        }
    }

    private void postEvent(String key) {
        EventBus.getDefault().post(new KeyEvent(key));
    }

}
