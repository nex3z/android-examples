package com.nex3z.examples.volleyexample.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.nex3z.examples.volleyexample.BuildConfig;
import com.nex3z.examples.volleyexample.R;
import com.nex3z.examples.volleyexample.model.Movie;
import com.nex3z.examples.volleyexample.model.MovieResponse;
import com.nex3z.examples.volleyexample.net.GsonRequest;
import com.nex3z.examples.volleyexample.net.VolleySingleton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final String TAG_FETCH_MOVIES = "tag_fetch_movies";

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private List<Movie> mMovies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.movie_grid);
        setupRecyclerView(mRecyclerView);
        fetchMovies();
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        mMovieAdapter = new MovieAdapter(mMovies);
        recyclerView.setAdapter(mMovieAdapter);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }

    private void fetchMovies() {
        Log.v(LOG_TAG, "fetchMovies()");
        String url =  "http://api.themoviedb.org/3/discover/movie?api_key=" + BuildConfig.API_KEY;
        GsonRequest<MovieResponse> request = new GsonRequest<>(url, MovieResponse.class, null,
                new Response.Listener<MovieResponse>() {
                    @Override
                    public void onResponse(MovieResponse response) {
                        Log.v(LOG_TAG, "onResponse(): size = " + response.getMovies().size());
                        mMovies.clear();
                        mMovies.addAll(response.getMovies());
                        mMovieAdapter.notifyItemInserted(0);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v(LOG_TAG, "onErrorResponse(): error = " + error);
                    }
        });
        request.setTag(TAG_FETCH_MOVIES);
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    @Override
    protected void onStop() {
        super.onStop();
        VolleySingleton.getInstance(this).cancelRequest(TAG_FETCH_MOVIES);
    }
}
