package com.nex3z.examples.retrofit2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.nex3z.examples.retrofit2.app.App;
import com.nex3z.examples.retrofit2.model.Movie;
import com.nex3z.examples.retrofit2.rest.model.MovieResponse;
import com.nex3z.examples.retrofit2.rest.service.MovieService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private ListView mListView;
    private ArrayAdapter<String> mMovieAdapter;
    private List<Movie> mMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView)findViewById(R.id.list);

        MovieService movieService = App.getRestClient().getMovieService();
        Call<MovieResponse> call =
                movieService.getMovies(MovieService.SORT_BY_POPULARITY_DESC);

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    MovieResponse movieResponse = response.body();
                    mMovies = movieResponse.getMovies();
                    Log.v(LOG_TAG, "onResponse(): mMovies size = " + mMovies.size());

                    updateListView();
                } else {
                    int statusCode = response.code();
                    Toast.makeText(MainActivity.this,
                            "Error code: " + statusCode, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateListView() {
        List<String> listContent = new ArrayList<>();
        for (Movie movie : mMovies) {
            listContent.add(movie.toString());
        }

        mMovieAdapter = new ArrayAdapter<>(
                MainActivity.this,
                R.layout.list_item,
                R.id.list_item_textview,
                listContent);
        mListView.setAdapter(mMovieAdapter);
    }
}
