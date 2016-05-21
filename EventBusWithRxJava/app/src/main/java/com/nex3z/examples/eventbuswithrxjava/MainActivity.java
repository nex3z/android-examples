package com.nex3z.examples.eventbuswithrxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private EventBus mEventBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_reset) {
            EventBus eventBus = getEventBusInstance();
            if (eventBus.hasObservers()) {
                eventBus.send(new CountEvent(0));
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public EventBus getEventBusInstance() {
        if (mEventBus == null) {
            mEventBus = new EventBus();
        }
        return mEventBus;
    }

}
