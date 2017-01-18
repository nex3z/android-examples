package com.nex3z.examples.realmexample.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.nex3z.examples.realmexample.R;
import com.nex3z.examples.realmexample.app.App;
import com.nex3z.examples.realmexample.model.Movie;
import com.nex3z.examples.realmexample.rest.model.MovieResponse;
import com.nex3z.examples.realmexample.rest.service.MovieService;
import com.nex3z.examples.realmexample.ui.adapter.MovieAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private MovieAdapter mMovieAdapter;
    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRealm();
        setupRecyclerView();
        setupFilter();
        fetchMovies();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

    private void initRealm() {
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.deleteRealm(realmConfiguration);
        mRealm = Realm.getInstance(realmConfiguration);
    }

    private void fetchMovies() {
        MovieService movieService = App.getRestClient().getMovieService();
        Call<MovieResponse> call =
                movieService.getMovies(MovieService.SORT_BY_POPULARITY_DESC);

        call.enqueue(new Callback<MovieResponse>() {

            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    MovieResponse movieResponse = response.body();
                    List<Movie> movies = movieResponse.getMovies();
                    Log.v(LOG_TAG, "onResponse(): movies size = " + movies.size());

                    mRealm.beginTransaction();
                    Collection<Movie> realmMovies = mRealm.copyToRealm(movies);
                    mRealm.commitTransaction();

                    mMovieAdapter.setMovieCollection(new ArrayList<>(realmMovies));
                } else {
                    int statusCode = response.code();
                    Log.e(LOG_TAG, "onResponse(): Error code = " + statusCode);
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupFilter() {
        EditText editText = (EditText) findViewById(R.id.keyword);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String keyword = charSequence.toString();
                if (!TextUtils.isEmpty(keyword)) {
                    Log.v(LOG_TAG, "onTextChanged(): keyword = " + keyword);
                    RealmResults<Movie> result = mRealm.where(Movie.class).contains("title", keyword, Case.INSENSITIVE).findAll();

                    List<Movie> movieList = new ArrayList<>();
                    for (Movie movie : result) {
                        movieList.add(movie);
                    }
                    Log.v(LOG_TAG, "onTextChanged(): result = " + result + ", " + result.getClass());
                    mMovieAdapter.setMovieCollection(movieList);
                } else {
                    RealmResults<Movie> movies = mRealm.where(Movie.class).findAll();
                    mMovieAdapter.setMovieCollection(movies);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.movie_grid);
        mMovieAdapter = new MovieAdapter();
        recyclerView.setAdapter(mMovieAdapter);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }

}
