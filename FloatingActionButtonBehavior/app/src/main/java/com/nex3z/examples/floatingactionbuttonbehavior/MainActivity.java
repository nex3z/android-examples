package com.nex3z.examples.floatingactionbuttonbehavior;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setAdapter(new SimpleAdapter(generateDummyData(20)));
    }

    private List<String> generateDummyData(int size) {
        List<String> data = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            data.add("Item " + i);
        }
        return data;
    }
}
