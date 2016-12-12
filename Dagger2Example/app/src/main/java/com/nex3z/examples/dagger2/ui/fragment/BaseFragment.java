package com.nex3z.examples.dagger2.ui.fragment;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.nex3z.examples.dagger2.internal.HasComponent;

public abstract class BaseFragment extends Fragment {
    private static final String LOG_TAG = BaseFragment.class.getSimpleName();

    private boolean mIsInjected = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            mIsInjected = onInjectView();
        } catch (IllegalStateException e) {
            Log.e(LOG_TAG, "onCreate(): " + e.getMessage());
            mIsInjected = false;
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mIsInjected)  {
            onViewInjected(savedInstanceState);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!mIsInjected) {
            mIsInjected = onInjectView();
            if (mIsInjected) {
                onViewInjected(savedInstanceState);
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected <C> C getComponent(Class<C> componentType) throws IllegalStateException {
        C component = componentType.cast(((HasComponent<C>) getActivity()).getComponent());
        if (component == null) {
            throw new IllegalStateException(componentType.getSimpleName()
                    + " has not been initialized yet.");
        }
        return component;
    }

    protected boolean onInjectView() throws IllegalStateException {
        return false;
    }


    @CallSuper
    protected void onViewInjected(Bundle savedInstanceState) {

    }
}