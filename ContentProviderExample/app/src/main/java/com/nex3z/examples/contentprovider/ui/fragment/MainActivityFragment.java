package com.nex3z.examples.contentprovider.ui.fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nex3z.examples.contentprovider.R;
import com.nex3z.examples.contentprovider.app.App;
import com.nex3z.examples.contentprovider.model.Movie;
import com.nex3z.examples.contentprovider.provider.MovieContract;
import com.nex3z.examples.contentprovider.rest.model.MovieResponse;
import com.nex3z.examples.contentprovider.rest.service.MovieService;
import com.nex3z.examples.contentprovider.ui.adapter.MovieAdapter;
import com.nex3z.examples.contentprovider.util.MovieDbUtility;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    public static final String[] MOVIE_COLUMNS = {
            MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_POSTER_PATH,
            MovieContract.MovieEntry.COLUMN_ID
    };

    public static final int COL_MOVIE_ID = 0;
    public static final int COL_MOVIE_TITLE = 1;
    public static final int COL_MOVIE_POSTER_PATH = 2;
    public static final int COL_MOVIE_MOVIE_ID = 3;

    private static final int MOVIE_LOADER = 0;

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;

    public MainActivityFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.movie_grid);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setupRecyclerView(mRecyclerView);
        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
        fetchMovies();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        Log.v(LOG_TAG, "onCreateLoader(): uri = " + uri);

        String sortOrder = MovieContract.MovieEntry.COLUMN_POPULARITY + " DESC";

        return new CursorLoader(getActivity(),
                uri,
                MOVIE_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v(LOG_TAG, "onLoadFinished(): data.getCount() = " + data.getCount());
        mMovieAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMovieAdapter.swapCursor(null);
    }

    private void fetchMovies() {
        MovieService movieService = App.getRestClient().getMovieService();
        Call<MovieResponse> call =
                movieService.getMovies(MovieService.SORT_BY_POPULARITY_DESC);

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Response<MovieResponse> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    MovieResponse movieResponse = response.body();
                    List<Movie> movies = movieResponse.getMovies();
                    Log.v(LOG_TAG, "onResponse(): movies size = " + movies.size());

                    getContext().getContentResolver().delete(
                            MovieContract.MovieEntry.CONTENT_URI,
                            null,
                            null
                    );
                    MovieDbUtility.addToDatabase(getContext(), movies);
                } else {
                    int statusCode = response.code();
                    Log.e(LOG_TAG, "onResponse(): Error code = " + statusCode);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        mMovieAdapter = new MovieAdapter();
        recyclerView.setAdapter(mMovieAdapter);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }

}
