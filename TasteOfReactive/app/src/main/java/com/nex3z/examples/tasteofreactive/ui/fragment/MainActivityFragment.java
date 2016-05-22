package com.nex3z.examples.tasteofreactive.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nex3z.examples.tasteofreactive.R;
import com.nex3z.examples.tasteofreactive.app.App;
import com.nex3z.examples.tasteofreactive.rest.model.User;
import com.nex3z.examples.tasteofreactive.rest.service.UserService;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivityFragment extends Fragment {
    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    private Subscription mGetUsersSub;
    private List<User> mUsers;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fetchUsers();
    }

    private void fetchUsers() {
        UserService userService = App.getRestClient().getUserService();

        mGetUsersSub = userService.getUsers()
                .timeout(5, TimeUnit.SECONDS)
                .retry(2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            mUsers = response;
                            Log.v(LOG_TAG, "mUsers size = " + mUsers.size());
                        },
                        throwable -> {
                            Toast.makeText(getActivity(),
                                    throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                );
    }
}
