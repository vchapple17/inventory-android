package com.example.valchapple.hybrid_android.controller;

import okhttp3.MediaType;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.valchapple.hybrid_android.activities.DeviceListActivity;
import com.example.valchapple.hybrid_android.activities.MainActivity;
import com.example.valchapple.hybrid_android.models.MyHttpClient;
import com.example.valchapple.hybrid_android.models.Device;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DeviceController extends AppCompatActivity {

//    public DeviceController(MyHttpClient c, DeviceListActivity parent) {
//        mParent = parent;
//        client = c;
//    }


    public static final Map<String, Device> DEVICE_MAP = new HashMap<String, Device>();
    public static List<Device> devices = new ArrayList<>();
    public static boolean isUpdating = false;
    public static MyHttpClient client;
//    public static final DeviceListActivity mParent;

    // STATIC HELPERS
    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    public static final String devicesURL = "https://hybrid-project-20180223.appspot.com/devices";
    public static final String[] model_types = {
            "LENOVO",
            "IPAD_4TH",
            "IPAD_AIR",
            "IPAD_AIR2"
    };
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
            e.printStackTrace();
            return null;
        }
        return d.toString();
    }
    public static String makePatchString(String model, String color, String serial_no) {
        JSONObject d = new JSONObject();
        try {
            if (model != null) {
                d.put("model", model);
            }
            if (color != null) {
                d.put("color", color);
            }
            if (serial_no != null) {
                d.put("serial_no", serial_no);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        if (d.length() < 1) {
            return null;
        }
        return d.toString();
    }
    private static String getDeviceUrlString(String device_id) {
        return devicesURL + "/" + device_id;
    }

    public static List<Device> getDevices() {
        // Ascending Order
        sortDevicesBySerial();
        return devices;
    }
    public static void sortDevicesBySerial() {
        Collections.sort(devices, new Comparator<Device>() {
            @Override
            public int compare(Device o1, Device o2) {
                String str1 = o1.serial;
                String str2 = o2.serial;
                int res = String.CASE_INSENSITIVE_ORDER.compare(str1, str2);
                if (res == 0) {
                    res = str1.compareTo(str2);
                }
                return res;
            }
        });
    }


    // GET Devices Request
    //, final DeviceListActivity.DeviceRecyclerViewAdapter a
    public static void requestDevices() {
        isUpdating = true;
        HttpUrl url = HttpUrl.parse(devicesURL);
        Request request = new Request.Builder().url(url).build();
        OkHttpClient okHttp = client.getOkHttpClient();
        okHttp.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                isUpdating = false;
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String r = response.body().string();

                isUpdating = true;
                DEVICE_MAP.clear();
                devices.clear();
                try {
                    JSONArray device_arr = new JSONArray(r);
                    Device d;
                    for(int i = 0; i < device_arr.length(); i++){
                        // Parse into Device
                        d = readJSONDevice(device_arr.getJSONObject(i));
                        DEVICE_MAP.put(d.id, d);
                        devices.add(d);
                    }
                    sortDevicesBySerial();
                    isUpdating = false;
//                    a.replaceDevices(devices);

//                    parent.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            //Handle UI here
//                            findViewById(R.id.loading).setVisibility(View.GONE);
//                        }
//                    });

                } catch (JSONException e) {
                    isUpdating = false;
                    e.printStackTrace();
                };
                isUpdating = false;
            }
        });

    }

    // POST Device Request
//    public boolean postDeviceDetails(MyHttpClient client, String serial, String model, String color) {
    public static boolean postDeviceDetails(String serial, String model, String color) {
        HttpUrl url = HttpUrl.parse(devicesURL);
        String postString = makePostString(model, color, serial);
        if (postString == null) {
            return false;
        }

        Log.d("postDeviceDetails", postString);
        RequestBody reqBody = RequestBody.create(MEDIA_TYPE_JSON, postString );

        Request request = new Request.Builder().post(reqBody).url(url).build();
        OkHttpClient okHttp = client.getOkHttpClient();

        try{
            Response response = okHttp.newCall(request).execute();
            String r = response.body().string();
            JSONObject device_obj = new JSONObject(r);
            Device d = readJSONDevice(device_obj);;
            DEVICE_MAP.put(d.id, d);
//            Device.devices.add(d);
            requestDevices();
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

    // PATCH Device Request
    public static boolean patchDeviceDetails(String device_id, String serial, String model, String color) {
        if ((device_id == null) || (device_id.length() < 1)) {
            return false;
        }
        String patchString = makePatchString(model, color, serial);
        if (patchString == null) {
            return false;
        }

//        String device_string = devicesURL + "/" + device_id;
        String device_string = getDeviceUrlString(device_id);
        HttpUrl url = HttpUrl.parse(device_string);

        RequestBody reqBody = RequestBody.create(MEDIA_TYPE_JSON, patchString);
        Request request = new Request.Builder().patch(reqBody).url(url).build();
        OkHttpClient okHttp = client.getOkHttpClient();

        try{
            Response response = okHttp.newCall(request).execute();
            String r = response.body().string();
            JSONObject device_obj = new JSONObject(r);
            Device d = readJSONDevice(device_obj);;
            DEVICE_MAP.replace(d.id, d);
//            DEVICE_MAP.put(d.id, d);
            int i = findIndexById(d.id);
            if (i == -1) {
                return false;
            }
            devices.set(findIndexById(d.id), d);
            sortDevicesBySerial();
//            requestDevices();
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

    // DELETE Device Request
    public static boolean deleteDevice(String device_id) {
        if ((device_id == null) || (device_id.length() < 1)) {
            return false;
        }

        String device_string = getDeviceUrlString(device_id);
        HttpUrl url = HttpUrl.parse(device_string);

        Request request = new Request.Builder().delete().url(url).build();
        OkHttpClient okHttp = client.getOkHttpClient();

        try{
            Response response = okHttp.newCall(request).execute();
            // verify device was delete
            if (response.code() == 204) {
                // Success
                requestDevices();
                return true;
            }
            else {
                // Failed to delete
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }

    }

    public static int findIndexById(String device_id) {
        int i = 0;
        for (i = 0; i < devices.size(); i++ ) {
            if (devices.get(i).id.equals(device_id)) {
                return i;
            }
        }
        return -1;
    }
}
