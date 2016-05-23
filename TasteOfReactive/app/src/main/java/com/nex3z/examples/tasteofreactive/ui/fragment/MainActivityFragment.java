package com.nex3z.examples.tasteofreactive.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.nex3z.examples.tasteofreactive.R;
import com.nex3z.examples.tasteofreactive.app.App;
import com.nex3z.examples.tasteofreactive.rest.service.UserService;
import com.nex3z.examples.tasteofreactive.ui.model.UserMapperDataMapper;
import com.nex3z.examples.tasteofreactive.ui.model.UserModel;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivityFragment extends Fragment {
    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    @BindView(R.id.btn_refresh_user_1)
    ImageButton mRefreshUser1;

    private Subscription mGetUsersSub;
    private List<UserModel> mUsers;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupObservables();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_fragment_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh_all: {
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupObservables() {
        UserMapperDataMapper mapper = new UserMapperDataMapper();
        UserService userService = App.getRestClient().getUserService();
        Observable<List<UserModel>> responseStream = userService.getUsers()
                .timeout(5, TimeUnit.SECONDS)
                .retry(2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(mapper::transform);

        Observable<Void> closeButton1Observable = RxView.clicks(mRefreshUser1);
        closeButton1Observable.subscribe(new Action1<Void>() {
            @Override
            public void call(Void x) {
            }
        });

        responseStream.subscribe(
                response -> {
                    mUsers = response;
                    Log.v(LOG_TAG, "mUsers size = " + mUsers.size());
                },
                throwable -> {
                    Toast.makeText(getActivity(),
                            throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
