package com.nex3z.examples.eventbusexample;

import android.os.Bundle;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private TextView mTvText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTvText = (TextView) findViewById(R.id.tv_text);
        mTvText.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(KeyEvent event) {
        Log.v(LOG_TAG, "onMessageEvent(): event = " + event);
        if (event.getKey().equals("C")) {
            mTvText.setText("");
        } else {
            mTvText.append(event.getKey());
            final int scrollAmount = mTvText.getLayout().getLineTop(mTvText.getLineCount()) - mTvText.getHeight();
            if (scrollAmount > 0)
                mTvText.scrollTo(0, scrollAmount);
            else
                mTvText.scrollTo(0, 0);
        }
    };
}
