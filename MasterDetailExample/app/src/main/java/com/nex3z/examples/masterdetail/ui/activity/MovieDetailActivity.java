package com.nex3z.examples.masterdetail.ui.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import com.nex3z.examples.masterdetail.R;
import com.nex3z.examples.masterdetail.model.Movie;
import com.nex3z.examples.masterdetail.ui.fragment.MovieDetailFragment;
import com.nex3z.examples.masterdetail.util.ImageUtility;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {
    private static final String LOG_TAG = MovieDetailActivity.class.getSimpleName();

    public static final String MOVIE_INFO = "MOVIE_INFO";

    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            mMovie = (Movie) getIntent().getSerializableExtra(MOVIE_INFO);
            Log.v(LOG_TAG, "onCreate(): mMovie = " + mMovie);

            MovieDetailFragment fragment = MovieDetailFragment.newInstance(mMovie);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragment)
                    .commit();

            supportPostponeEnterTransition();

            updateTitle();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            supportFinishAfterTransition();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateTitle() {
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        appBarLayout.setTitle(mMovie.getTitle());

        ImageView backdrop = (ImageView) findViewById(R.id.detail_backdrop);
        String url = ImageUtility.getBackdropImageUrl(mMovie.getBackdropPath());
        Picasso.with(this).load(url).into(backdrop, new com.squareup.picasso.Callback() {
            @Override
            public void onError() {
                supportStartPostponedEnterTransition();
            }

            @Override
            public void onSuccess() {
                supportStartPostponedEnterTransition();
            }
        });
    }
}
