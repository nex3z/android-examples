package com.nex3z.examples.dagger2.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.nex3z.examples.dagger2.R;
import com.nex3z.examples.dagger2.app.App;
import com.nex3z.examples.dagger2.internal.HasComponent;
import com.nex3z.examples.dagger2.internal.component.DaggerMainComponent;
import com.nex3z.examples.dagger2.internal.component.MainComponent;
import com.nex3z.examples.dagger2.internal.module.MainModule;
import com.nex3z.examples.dagger2.internal.module.RestModule;
import com.nex3z.examples.dagger2.ui.MovieGridView;

public class MainActivity extends BaseActivity implements HasComponent<MainComponent> {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final String BASE_URL = "http://api.themoviedb.org";

    private MainComponent mMainComponent;
    private MovieGridView mMovieGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mMovieGridView = (MovieGridView) getSupportFragmentManager().findFragmentById(R.id.fragment);
        Log.v(LOG_TAG, "onCreate(): mMovieGridView = " + mMovieGridView);

        initInjector();
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

    private void initInjector() {
        mMainComponent = DaggerMainComponent.builder()
                .appComponent(((App)getApplication()).getAppComponent())
                .mainModule(new MainModule(mMovieGridView))
                .restModule(new RestModule(BASE_URL))
                .build();
    }

    @Override
    public MainComponent getComponent() {
        return mMainComponent;
    }

}
