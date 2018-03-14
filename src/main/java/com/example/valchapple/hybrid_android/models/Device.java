package com.example.valchapple.hybrid_android.models;

import android.os.StrictMode;
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
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
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
    public boolean is_rented;
    public Device(String id, boolean is_rented, String serial, String model, String color) {
        this.id = id;
        this.serial = serial;
        this.model = model;
        this.color = color;
        this.is_rented = is_rented;
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




    // STATIC
    //    MediaType.parse(String mediaType)
    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    public static final String devicesURL = "https://hybrid-project-20180223.appspot.com/devices";
    public static final Map<String, Device> DEVICE_MAP = new HashMap<String, Device>();
    public static List<Device> devices = new ArrayList<>();
    public static List<Device> getDevices() {
        return devices;
    }
    public static final String[] model_types = {
            "LENOVO",
            "IPAD_4TH",
            "IPAD_AIR",
            "IPAD_AIR2"
    };

    // JSON HELPER
    public static Device readJSONDevice(JSONObject device_JSON) throws JSONException {
        Log.d("readJSONDevice", device_JSON.toString());
        String id = device_JSON.getString("id");

        boolean is_rented = Boolean.parseBoolean(device_JSON.getString("is_rented"));
        String serial = device_JSON.getString("serial_no");
        String model = device_JSON.getString("model");
        String color = device_JSON.getString("color");

        return new Device(id, is_rented, serial, model, color);
    }
    public static String makePostString(String model, String color, String serial) {
        JSONObject d = new JSONObject();
        try {
            d.put("model", model);
            d.put("color", color);
            d.put("serial_no", serial);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return d.toString();
    }

    // GET Devices Request
    public static void requestDevices( MyHttpClient client ) {
        HttpUrl url = HttpUrl.parse(devicesURL);
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

    // POST Device Request
    public static boolean postDeviceDetails(MyHttpClient client, String serial, String model, String color) {
        HttpUrl url = HttpUrl.parse(devicesURL);
        String postString = makePostString(model, color, serial);
        Log.d("postDeviceDetails", postString);
        RequestBody reqBody = RequestBody.create(MEDIA_TYPE_JSON, postString );

        Request request = new Request.Builder().post(reqBody).url(url).build();
        OkHttpClient okHttp = client.getOkHttpClient();

        try{
            Response response = okHttp.newCall(request).execute();
            String r = response.body().string();
            JSONObject device_obj = new JSONObject(r);
            Device d = readJSONDevice(device_obj);;
            Device.DEVICE_MAP.put(d.id, d);
            Device.devices.add(d);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }



    public static boolean patchDeviceDetails(MyHttpClient client, String serial, String model, String color) {
        // Return 1 for successful save
        // Return 0 for failed save
//        HttpUrl url = HttpUrl.parse(devicesURL);
//
//        RequestBody reqBody = RequestBody(MEDIA_TYPE_JSON),
//        Request request = new Request.Builder().url(url).build();
//
//        OkHttpClient okHttp = client.getOkHttpClient();
//        try {
//            Response response = okHttp.newCall(request).execute();
//            String r = response.body().string();
//            JSONObject device_obj = new JSONObject(r);
//            Device d = readJSONDevice(device_obj);;
//            Device.DEVICE_MAP.put(d.id, d);
//            Device.devices.add(d);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
//        catch (JSONException e) {
//            e.printStackTrace();
//            return false;
//        };

        return true;
    }
}
