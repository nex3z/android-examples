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
import com.nex3z.examlpes.mvp.data.entity.mapper.MovieEntityDataMapper;
import com.nex3z.examlpes.mvp.data.executor.JobExecutor;
import com.nex3z.examlpes.mvp.data.repository.MovieDataRepository;
import com.nex3z.examlpes.mvp.data.repository.datasource.MovieDataStoreFactory;
import com.nex3z.examlpes.mvp.data.rest.RestClient;
import com.nex3z.examlpes.mvp.domain.executor.PostExecutionThread;
import com.nex3z.examlpes.mvp.domain.executor.ThreadExecutor;
import com.nex3z.examlpes.mvp.domain.interactor.GetMovieList;
import com.nex3z.examlpes.mvp.presentation.UIThread;
import com.nex3z.examlpes.mvp.presentation.mapper.MovieModelDataMapper;
import com.nex3z.examlpes.mvp.presentation.model.MovieModel;
import com.nex3z.examlpes.mvp.presentation.presenter.MovieListPresenter;
import com.nex3z.examlpes.mvp.presentation.view.MovieListView;
import com.nex3z.examlpes.mvp.presentation.view.adapter.MovieAdapter;
import com.nex3z.examlpes.mvp.presentation.view.misc.EndlessRecyclerOnScrollListener;
import com.nex3z.examlpes.mvp.presentation.view.misc.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;

public class MainActivityFragment extends BaseFragment implements MovieListView {
    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private static final int FIRST_PAGE = 1;

    private MovieAdapter mMovieAdapter;
    private List<MovieModel> mMovies = new ArrayList<>();
    private EndlessRecyclerOnScrollListener mOnScrollListener;

    private MovieListPresenter mMovieListPresenter;

    @Bind(R.id.movie_grid)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipe_container)
    SwipeRefreshLayout mSwipeLayout;
    @Bind(R.id.progressbar) ProgressBar mProgressBar;

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

        initPresenter();
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

    private void initPresenter() {
        ThreadExecutor threadExecutor = JobExecutor.getInstance();
        PostExecutionThread postExecutionThread = new UIThread();

        RestClient restClient = new RestClient();
        MovieDataStoreFactory movieDataStoreFactory = new MovieDataStoreFactory(restClient.getRetrofit());
        MovieEntityDataMapper movieEntityDataMapper = new MovieEntityDataMapper();
        MovieDataRepository movieDataRepository = new MovieDataRepository(movieDataStoreFactory, movieEntityDataMapper);

        GetMovieList getMovieList = new GetMovieList(movieDataRepository, threadExecutor, postExecutionThread);
        MovieModelDataMapper movieModelDataMapper = new MovieModelDataMapper();
        mMovieListPresenter = new MovieListPresenter(getMovieList, movieModelDataMapper);
        mMovieListPresenter.setView(this);

    }

    private void fetchInitialMovies() {
        mMovies.clear();
        if (mMovieAdapter != null) {
            mMovieAdapter.notifyDataSetChanged();
        }
        mMovieListPresenter.getMovieList(FIRST_PAGE);
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
                mMovieListPresenter.getMovieList(currentPage);
                Log.v(LOG_TAG, "onLoadMore(): currentPage = " + currentPage
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
    public void renderMovieList(Collection<MovieModel> movieModelCollection) {

    }

    @Override
    public void appendMovieList(Collection<MovieModel> movieModelCollection) {
        Log.v(LOG_TAG, "appendMovies(): movies.size() = " + movieModelCollection.size());

        mMovies.addAll(movieModelCollection);

        int insertSize = movieModelCollection.size();
        int insertPos = mMovies.size() - insertSize;
        Log.v(LOG_TAG, "appendMovies(): insertPos = " + insertPos
                + ", insertSize = " + insertSize);
        mMovieAdapter.notifyItemRangeInserted(insertPos, insertSize);
    }

    @Override
    public void showLoading() {
        startRefreshAnimate();
    }

    @Override
    public void hideLoading() {
        stopRefreshAnimate();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
