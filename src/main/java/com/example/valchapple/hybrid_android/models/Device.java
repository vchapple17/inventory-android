package com.example.valchapple.hybrid_android.models;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by valchapple on 3/6/18.
 * Class Model to use within app
 */

public class Device {
    public final String id;
    public String serial;
    public String model;
    public String color;
    public int status;
//    public static List<Device> devices;


    public Device(String id, int status, String serial, String model, String color) {
        this.id = id;
        this.serial = serial;
        this.model = model;
        this.color = color;
        this.status = status;
    }

    public static List<Device> updateDevices() {
        // Make HTTP call to get devices
//        List<Device> devices = [Device("id", "status", "serial", "model", "color")];
        return new ArrayList<Device>();
    }

//    // GET Devices Request
    static public void getDevices( MyHttpClient client, HttpUrl url ) {
//      MyHttpClient client = (MyHttpClient)getApplicationContext().client
        Log.d("Device Model", "getDevices");
        Request request = new Request.Builder().url(url).build();
        OkHttpClient okHttp = client.getOkHttpClient();
        okHttp.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String r = response.body().string();
                Log.d("Device Model", r);
            }
        });

    }

    @Override
    public String toString() {
        return serial;
    }
}
