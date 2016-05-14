package com.nex3z.examples.simplecustomview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private VerticalScrollView mContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        mContainer = (VerticalScrollView) findViewById(R.id.container);
        LayoutInflater inflater = getLayoutInflater();
        Random rnd = new Random();
        for (int i = 0; i < 10; ++i) {
            ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.item, mContainer, false);

            BlockView blockView = (BlockView) layout.findViewById(R.id.block);
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            blockView.setBlockColor(color);

            mContainer.addView(layout);
        }
    }
}
