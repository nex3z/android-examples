package com.nex3z.examlpes.mvp.presentation.view.fragment;


import android.support.v4.app.Fragment;

import com.nex3z.examlpes.mvp.presentation.internal.HasComponent;


public class BaseFragment extends Fragment {

    @SuppressWarnings("unchecked")
    protected <C> C getComponent(Class<C> componentType) {
        return componentType.cast(((HasComponent<C>)getActivity()).getComponent());
    }

}
