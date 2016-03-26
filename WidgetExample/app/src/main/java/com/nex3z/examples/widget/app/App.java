package com.nex3z.examples.widget.app;

import android.app.Application;

import com.nex3z.examples.widget.rest.RestClient;

public class App extends Application {
    private static RestClient restClient;

    @Override
    public void onCreate() {
        super.onCreate();
        restClient = new RestClient();
    }

    public static RestClient getRestClient() {
        return restClient;
    }
}
