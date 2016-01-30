package com.nex3z.examlpes.mvp.presentation.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nex3z.examlpes.mvp.R;
import com.nex3z.examlpes.mvp.data.entity.MovieEntity;
import com.nex3z.examlpes.mvp.presentation.internal.component.MainComponent;
import com.nex3z.examlpes.mvp.presentation.presenter.MainPresenter;
import com.nex3z.examlpes.mvp.presentation.view.MovieGridView;
import com.nex3z.examlpes.mvp.presentation.view.adapter.MovieAdapter;
import com.nex3z.examlpes.mvp.presentation.view.misc.EndlessRecyclerOnScrollListener;
import com.nex3z.examlpes.mvp.presentation.view.misc.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;

public class MainActivityFragment extends BaseFragment implements MovieGridView {
    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private static final int FIRST_PAGE = 1;

    private MovieAdapter mMovieAdapter;
    private List<MovieEntity> mMovies = new ArrayList<>();
    private EndlessRecyclerOnScrollListener mOnScrollListener;

    @Bind(R.id.movie_grid)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipe_container)
    SwipeRefreshLayout mSwipeLayout;
    @Bind(R.id.progressbar) ProgressBar mProgressBar;

    @Inject
    MainPresenter mMainPresenter;

    public MainActivityFragment() { }

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

        getComponent(MainComponent.class).inject(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        setupRecyclerView(mRecyclerView);

        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mOnScrollListener != null) {
                    mOnScrollListener.reset();
                }
                fetchInitialMovies();
            }
        });

        fetchInitialMovies();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void fetchInitialMovies() {
        mMovies.clear();
        if (mMovieAdapter != null) {
            mMovieAdapter.notifyDataSetChanged();
        }
        mMainPresenter.fetchMovies(FIRST_PAGE);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        mMovieAdapter = new MovieAdapter(mMovies);
        SlideInBottomAnimationAdapter animationAdapter =
                new SlideInBottomAnimationAdapter(mMovieAdapter);
        animationAdapter.setDuration(500);
        recyclerView.setAdapter(animationAdapter);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.addItemDecoration(new SpacesItemDecoration(4, 4, 4, 4));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        mOnScrollListener = new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                mMainPresenter.fetchMovies(currentPage);
                Log.v(LOG_TAG, "onLoadMore(): current_page = " + currentPage
                        + ", mMovies.size() = " + mMovies.size());
            }
        };
        recyclerView.addOnScrollListener(mOnScrollListener);
    }

    private void startRefreshAnimate() {
        mSwipeLayout.setRefreshing(true);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void stopRefreshAnimate() {
        mSwipeLayout.setRefreshing(false);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void toastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        startRefreshAnimate();
    }

    @Override
    public void hideProgress() {
        stopRefreshAnimate();
    }

    @Override
    public void appendMovies(List<MovieEntity> movies) {
        Log.v(LOG_TAG, "appendMovies(): movies.size() = " + movies.size());

        mMovies.addAll(movies);

        int insertSize = movies.size();
        int insertPos = mMovies.size() - insertSize;
        Log.v(LOG_TAG, "appendMovies(): insertPos = " + insertPos
                + ", insertSize = " + insertSize);
        mMovieAdapter.notifyItemRangeInserted(insertPos, insertSize);
    }
}
