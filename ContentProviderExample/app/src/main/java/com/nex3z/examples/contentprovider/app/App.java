package com.nex3z.examples.contentprovider.app;

import android.app.Application;

import com.nex3z.examples.contentprovider.rest.RestClient;

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
