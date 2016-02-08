package com.nex3z.examlpes.mvp.presentation.view.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.nex3z.examlpes.mvp.R;
import com.nex3z.examlpes.mvp.presentation.internal.HasComponent;
import com.nex3z.examlpes.mvp.presentation.internal.component.DaggerMovieComponent;
import com.nex3z.examlpes.mvp.presentation.internal.component.MovieComponent;
import com.nex3z.examlpes.mvp.presentation.view.MovieListView;


public class MainActivity extends BaseActivity implements HasComponent<MovieComponent> {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private MovieComponent mMovieComponent;
    private MovieListView mMovieListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeInjector();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mMovieListView = (MovieListView) getSupportFragmentManager().findFragmentById(R.id.fragment);
        Log.v(LOG_TAG, "onCreate(): mMovieListView = " + mMovieListView);

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
    public MovieComponent getComponent() {
        return mMovieComponent;
    }

    private void initializeInjector() {
        mMovieComponent = DaggerMovieComponent.builder()
                .appComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build();
    }

}
