package com.nex3z.examples.retrofitwithrxjava.app;

import android.app.Application;

import com.nex3z.examples.retrofitwithrxjava.rest.RestClient;

public class App extends Application {
    private static RestClient sRestClient;

    @Override
    public void onCreate() {
        super.onCreate();
        sRestClient = new RestClient();
    }

    public static RestClient getRestClient() {
        return sRestClient;
    }
}
