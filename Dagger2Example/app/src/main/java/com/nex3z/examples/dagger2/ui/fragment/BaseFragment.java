package com.nex3z.examples.dagger2.ui.fragment;

import android.support.v4.app.Fragment;

import com.nex3z.examples.dagger2.internal.HasComponent;

public class BaseFragment extends Fragment {

    @SuppressWarnings("unchecked")
    protected <C> C getComponent(Class<C> componentType) {
        return componentType.cast(((HasComponent<C>)getActivity()).getComponent());
    }

}
