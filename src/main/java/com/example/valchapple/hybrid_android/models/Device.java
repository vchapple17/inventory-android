package com.example.valchapple.hybrid_android.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static final Map<String, Device> DEVICE_MAP = new HashMap<String, Device>();


    public final String id;
    public String serial;
    public String model;
    public String color;
    public boolean is_rented;
    public static List<Device> devices = new ArrayList<>();

    static public List<Device> getDevices() {
        return devices;
    }

    public Device(String id, boolean is_rented, String serial, String model, String color) {
        this.id = id;
        this.serial = serial;
        this.model = model;
        this.color = color;
        this.is_rented = is_rented;
    }

//    // GET Devices Request
    static public void requestDevices( MyHttpClient client, HttpUrl url ) {
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
                Device.DEVICE_MAP.clear();
                Device.devices.clear();
                try {
                    JSONArray device_arr = new JSONArray(r);
                    Device d = null;
                    for(int i = 0; i < device_arr.length(); i++){
                        // Parse into Device
                        d = readJSONDevice(device_arr.getJSONObject(i));
                        Device.DEVICE_MAP.put(d.id, d);
                        Device.devices.add(d);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                };

            }
        });

    }

    public static Device readJSONDevice(JSONObject device_JSON) throws JSONException {
        Log.d("readJSONDevice", device_JSON.toString());
        String id = device_JSON.getString("id");

        boolean is_rented = Boolean.parseBoolean(device_JSON.getString("is_rented"));
        String serial = device_JSON.getString("serial_no");
        String model = device_JSON.getString("model");
        String color = device_JSON.getString("color");

        return new Device(id, is_rented, serial, model, color);
    }


    public String getModelText() {
        return model.toString();
    }

    public String getColorText() {
        return color.toString();
    }

    public String getSerialText() {
        return serial.toString();
    }

    public String getStatusText() {
        if (is_rented == true) {
            return "CHECKED OUT";
        }
        else {
            return "AVAILABLE";
        }
    }
}
