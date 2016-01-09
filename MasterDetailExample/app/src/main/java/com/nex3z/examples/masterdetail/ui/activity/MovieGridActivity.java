package com.nex3z.examples.masterdetail.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.nex3z.examples.masterdetail.R;
import com.nex3z.examples.masterdetail.model.Movie;
import com.nex3z.examples.masterdetail.ui.adapter.MovieAdapter;
import com.nex3z.examples.masterdetail.ui.fragment.MovieDetailFragment;
import com.nex3z.examples.masterdetail.ui.fragment.MovieGridFragment;


public class MovieGridActivity extends AppCompatActivity implements MovieGridFragment.Callbacks{
    private static final String LOG_TAG = MovieGridActivity.class.getSimpleName();

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_grid);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
        } else {
            mTwoPane = false;
        }
        Log.v(LOG_TAG, "onCreate(): mTwoPane = " + mTwoPane);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(Movie movie, MovieAdapter.ViewHolder vh) {
        Log.v(LOG_TAG, "onItemSelected(): movie = " + movie);

        if (mTwoPane) {
            MovieDetailFragment fragment = MovieDetailFragment.newInstance(movie);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, MovieDetailActivity.class)
                    .putExtra(MovieDetailActivity.MOVIE_INFO, movie);

            ActivityOptionsCompat activityOptions = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(this, new Pair<View, String>(
                                    vh.mPoster,
                                    getString(R.string.detail_poster_transition_name)));

            ActivityCompat.startActivity(this, intent, activityOptions.toBundle());
        }
    }
}
