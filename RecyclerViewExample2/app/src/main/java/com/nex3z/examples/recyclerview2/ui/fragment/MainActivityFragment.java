package com.nex3z.examples.recyclerview2.ui.fragment;

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

import com.nex3z.examples.recyclerview2.R;
import com.nex3z.examples.recyclerview2.app.App;
import com.nex3z.examples.recyclerview2.model.Movie;
import com.nex3z.examples.recyclerview2.rest.model.MovieResponse;
import com.nex3z.examples.recyclerview2.rest.service.MovieService;
import com.nex3z.examples.recyclerview2.ui.EndlessRecyclerOnScrollListener;
import com.nex3z.examples.recyclerview2.ui.SpacesItemDecoration;
import com.nex3z.examples.recyclerview2.ui.adapter.MovieAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivityFragment extends Fragment {
    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private static final int FIRST_PAGE = 1;

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeLayout;
    private ProgressBar mProgressBar;

    private MovieAdapter mMovieAdapter;
    private List<Movie> mMovies = new ArrayList<>();
    private int mPage = FIRST_PAGE;

    public MainActivityFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.movie_grid);
        mSwipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressbar);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setupRecyclerView(mRecyclerView);

        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchMovies();
            }
        });

        fetchMovies();
    }

    private void fetchMovies() {
        mMovies.clear();
        fetchMovies(FIRST_PAGE);
    }

    private void fetchMovies(int page) {
        MovieService movieService = App.getRestClient().getMovieService();
        Call<MovieResponse> call =
                movieService.getMovies(MovieService.SORT_BY_POPULARITY_DESC, page);

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Response<MovieResponse> response, Retrofit retrofit) {
                MovieResponse movieResponse = response.body();
                List<Movie> movies = movieResponse.getMovies();
                Log.v(LOG_TAG, "onResponse(): movies size = " + movies.size());

                mMovies.addAll(movies);
                mMovieAdapter.notifyDataSetChanged();

                mSwipeLayout.setRefreshing(false);
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Throwable t) {
                mSwipeLayout.setRefreshing(false);
                mProgressBar.setVisibility(View.GONE);

                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        mMovieAdapter = new MovieAdapter(mMovies);
        mRecyclerView.setAdapter(mMovieAdapter);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.addItemDecoration(new SpacesItemDecoration(4, 4, 4, 4));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                fetchMovies(++mPage);
                Log.v(LOG_TAG, "onLoadMore(): mMovies updated, size = " + mMovies.size());
            }
        });
    }
}
