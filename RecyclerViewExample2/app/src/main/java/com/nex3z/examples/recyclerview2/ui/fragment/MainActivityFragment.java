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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.nex3z.examples.recyclerview2.R;
import com.nex3z.examples.recyclerview2.app.App;
import com.nex3z.examples.recyclerview2.model.Movie;
import com.nex3z.examples.recyclerview2.rest.model.MovieResponse;
import com.nex3z.examples.recyclerview2.rest.service.MovieService;
import com.nex3z.examples.recyclerview2.ui.adapter.MovieAdapter;
import com.nex3z.examples.recyclerview2.ui.misc.EndlessRecyclerOnScrollListener;
import com.nex3z.examples.recyclerview2.ui.misc.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
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
    private Spinner mSpinner;

    private MovieAdapter mMovieAdapter;
    private List<Movie> mMovies = new ArrayList<>();
    private EndlessRecyclerOnScrollListener mOnScrollListener;
    private Call<MovieResponse> mCall;
    private String mSort = MovieService.SORT_BY_POPULARITY_DESC;

    public MainActivityFragment() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupSpinner();
    }

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
                if (mOnScrollListener != null) {
                    mOnScrollListener.reset();
                }
                if (mCall != null) {
                    mCall.cancel();
                }
                fetchInitialMovies(mSort);
            }
        });

        fetchInitialMovies(mSort);
    }

    private void setupSpinner() {
        mSpinner = (Spinner) getActivity().findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.sort_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.v(LOG_TAG, "onItemSelected(): position = " + position);
                switch (position) {
                    case 1:
                        mSort = MovieService.SORT_BY_POPULARITY_DESC;
                        break;
                    case 2:
                        mSort = MovieService.SORT_BY_VOTE_AVERAGE_DESC;
                        break;
                    case 3:
                        mSort = MovieService.SORT_BY_VOTE_COUNT_DESC;
                        break;
                }
                fetchInitialMovies(mSort);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void fetchInitialMovies(String sort) {
        mMovies.clear();
        if (mMovieAdapter != null) {
            mMovieAdapter.notifyDataSetChanged();
        }
        mOnScrollListener.reset();

        fetchMovies(FIRST_PAGE, sort);
    }

    private void fetchMovies(int page, String sort) {
        Log.v(LOG_TAG, "fetchMovies(): page = " + page);
        MovieService movieService = App.getRestClient().getMovieService();
        mCall = movieService.getMovies(sort, page);

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
            public void onLoadMore(int currentPage) {
                fetchMovies(currentPage, mSort);
                Log.v(LOG_TAG, "onLoadMore(): currentPage = " + currentPage
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
