package com.nex3z.examples.dagger2.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.nex3z.examples.dagger2.R;
import com.nex3z.examples.dagger2.app.App;
import com.nex3z.examples.dagger2.internal.HasComponent;
import com.nex3z.examples.dagger2.internal.component.DaggerRestComponent;
import com.nex3z.examples.dagger2.internal.component.RestComponent;
import com.nex3z.examples.dagger2.internal.module.RestModule;

public class MovieGridActivity extends BaseActivity implements HasComponent<RestComponent> {

    private static final String BASE_URL = "http://api.themoviedb.org";

    private RestComponent mRestComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_grid);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        mRestComponent = DaggerRestComponent.builder()
                .appComponent(((App)getApplication()).getAppComponent())
                .restModule(new RestModule(BASE_URL))
                .build();
    }

    @Override
    public RestComponent getComponent() {
        return mRestComponent;
    }
}
