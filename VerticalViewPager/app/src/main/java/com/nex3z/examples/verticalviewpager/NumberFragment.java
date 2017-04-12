package com.nex3z.examples.verticalviewpager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class NumberFragment extends Fragment {
    private static final String ARG_NUMBER = "number";

    private int mNumber;
    private TextView mTvNumber;

    public NumberFragment() {}

    public static NumberFragment newInstance(int number) {
        NumberFragment fragment = new NumberFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_NUMBER, number);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mNumber = getArguments().getInt(ARG_NUMBER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_number, container, false);
        mTvNumber = (TextView) rootView.findViewById(R.id.tv_number);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mTvNumber.setText(String.valueOf(mNumber));
    }

}
