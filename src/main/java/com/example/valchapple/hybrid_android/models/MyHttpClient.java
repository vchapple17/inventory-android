package com.example.valchapple.hybrid_android.models;

import android.app.Application;


import okhttp3.OkHttpClient;

/**
 * Created by valchapple on 3/12/18.
 */

public class MyHttpClient extends Application {
    private static OkHttpClient client;
//    private static final MediaType MEDIA_JSON = MediaType.parse("application/json; charset=utf-8");

    public void onCreate() {
        super.onCreate();
        client = new OkHttpClient();
//        new OkHttpClient(getApplicationContext());
//        new OkHttpClient(InitState state);
    }
    static public OkHttpClient getOkHttpClient() {
        return client;
    }

}
