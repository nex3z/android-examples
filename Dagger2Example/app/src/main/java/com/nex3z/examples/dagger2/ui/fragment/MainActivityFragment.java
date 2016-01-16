package com.nex3z.examples.dagger2.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nex3z.examples.dagger2.R;
import com.nex3z.examples.dagger2.internal.HasComponent;
import com.nex3z.examples.dagger2.internal.component.RestComponent;
import com.nex3z.examples.dagger2.model.Movie;
import com.nex3z.examples.dagger2.rest.model.MovieResponse;
import com.nex3z.examples.dagger2.rest.service.MovieService;
import com.nex3z.examples.dagger2.ui.adapter.MovieAdapter;
import com.nex3z.examples.dagger2.ui.misc.EndlessRecyclerOnScrollListener;
import com.nex3z.examples.dagger2.ui.misc.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivityFragment extends Fragment {
    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private static final int FIRST_PAGE = 1;

    private MovieAdapter mMovieAdapter;
    private List<Movie> mMovies = new ArrayList<>();
    private EndlessRecyclerOnScrollListener mOnScrollListener;
    private Call<MovieResponse> mCall;

    @Bind(R.id.movie_grid) RecyclerView mRecyclerView;
    @Bind(R.id.swipe_container) SwipeRefreshLayout mSwipeLayout;
    @Bind(R.id.progressbar) ProgressBar mProgressBar;

    @Inject MovieService mMovieService;

    public MainActivityFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((HasComponent<RestComponent>)getActivity()).getComponent().inject(this);
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
                if (mCall != null) {
                    mCall.cancel();
                }
                fetchMovies();
            }
        });

        fetchMovies();
    }

    private void fetchMovies() {
        mMovies.clear();
        if (mMovieAdapter != null) {
            mMovieAdapter.notifyDataSetChanged();
        }
        fetchMovies(FIRST_PAGE);
    }

    private void fetchMovies(int page) {
        Log.v(LOG_TAG, "fetchMovies(): page = " + page);
        mCall = mMovieService.getMovies(MovieService.SORT_BY_POPULARITY_DESC, page);
        mCall.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Response<MovieResponse> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    MovieResponse movieResponse = response.body();
                    List<Movie> movies = movieResponse.getMovies();
                    Log.v(LOG_TAG, "onResponse(): movies size = " + movies.size());

                    mMovies.addAll(movies);

                    int insertSize = movies.size();
                    int insertPos = mMovies.size() - insertSize;
                    Log.v(LOG_TAG, "onResponse(): insertPos = " + insertPos
                            + ", insertSize = " + insertSize);
                    mMovieAdapter.notifyItemRangeInserted(insertPos, insertSize);
                } else {
                    int statusCode = response.code();
                    Log.e(LOG_TAG, "onResponse(): Error code = " + statusCode);
                }
                stopRefreshAnimate();
            }

            @Override
            public void onFailure(Throwable t) {
                stopRefreshAnimate();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
            public void onLoadMore(int current_page) {
                fetchMovies(current_page);
                Log.v(LOG_TAG, "onLoadMore(): current_page = " + current_page
                        + ", mMovies.size() = " + mMovies.size());
            }
        };
        recyclerView.addOnScrollListener(mOnScrollListener);
    }

    private void stopRefreshAnimate() {
        mSwipeLayout.setRefreshing(false);
        mProgressBar.setVisibility(View.GONE);
    }
}
