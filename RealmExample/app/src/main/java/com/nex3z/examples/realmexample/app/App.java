package com.nex3z.examples.realmexample.app;

import android.app.Application;

import com.nex3z.examples.realmexample.rest.RestClient;

import io.realm.Realm;


public class App extends Application {
    private static RestClient restClient;

    @Override
    public void onCreate() {
        super.onCreate();
        restClient = new RestClient();
        Realm.init(this);
    }

    public static RestClient getRestClient() {
        return restClient;
    }
}
