package com.nex3z.examples.eventbuswithrxjava;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class MainActivityFragment extends Fragment {
    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    private EventBus mEventBus;
    private CompositeSubscription mSubscriptions;
    private TextView mCountDisplay;
    private int mCount = 0;

    public MainActivityFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mCountDisplay = (TextView) rootView.findViewById(R.id.tv_count);
        mCountDisplay.setText(String.valueOf(mCount));

        Button button = (Button) rootView.findViewById(R.id.btn_count);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCount++;
                if (mEventBus.hasObservers()) {
                    mEventBus.send(new CountEvent(mCount));
                }
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mEventBus = ((MainActivity) getActivity()).getEventBusInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        mSubscriptions =  new CompositeSubscription();
        mSubscriptions.add(mEventBus.toObservable()
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object event) {
                        if (event instanceof CountEvent) {
                            mCount = ((CountEvent) event).getCount();
                            updateCount(mCount);
                        }
                    }
                }));
    }

    @Override
    public void onStop() {
        super.onStop();
        mSubscriptions.clear();
    }

    private void updateCount(int count) {
        mCountDisplay.setText(String.valueOf(count));
    }

}
