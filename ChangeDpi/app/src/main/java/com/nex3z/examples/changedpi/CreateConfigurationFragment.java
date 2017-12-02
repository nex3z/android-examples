package com.nex3z.examples.changedpi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CreateConfigurationFragment extends Fragment {

    public CreateConfigurationFragment() {}

    public static CreateConfigurationFragment newInstance() {
        return new CreateConfigurationFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_configuration, container, false);
    }

}
