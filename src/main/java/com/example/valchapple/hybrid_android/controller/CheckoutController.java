package com.example.valchapple.hybrid_android.controller;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.valchapple.hybrid_android.models.MyHttpClient;
import com.example.valchapple.hybrid_android.models.User;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

// TODO Update Views to show start date for device checkout
// TODO Implement Check in button when User returns device

public class CheckoutController extends AppCompatActivity {
    public static MyHttpClient client;

    // CONSTANTS
    private static final String usersURL = "https://hybrid-project-20180223.appspot.com/users";
    private static final String devicesPATH = "/devices";

    private static String _getCheckoutUrlString(String user_id, String device_id) {
        return usersURL + "/" + user_id + devicesPATH + "/" + device_id  ;
    }

    // PUT a DEVICE to a USER
    public static boolean checkoutDevice(String user_id, String device_id) {
        if ((user_id == null) || (user_id.length() < 1)) {
            return false;
        }
        if ((device_id == null) || (device_id.length() < 1)) {
            return false;
        }
        HttpUrl url = HttpUrl.parse(_getCheckoutUrlString(user_id, device_id));
        Log.d("checkoutDevice", "");
        RequestBody reqBody = RequestBody.create(null, new byte[0]);
        Request request = new Request.Builder().put(reqBody).url(url).build();
        OkHttpClient okHttp = client.getOkHttpClient();

        try{
            Response response = okHttp.newCall(request).execute();
            if (response.code() == 204) {
                int i = UserController.findIndexById(user_id);
                if (i != -1) {
                    User u = UserController.users.get(i);
                    u.device_id = device_id;
                    UserController.requestUsers();
                    assert (UserController.users.get(i).device_id != null);
                }
                User u = UserController.USER_MAP.get(user_id);
                if (u != null) {
                    u.device_id = device_id;
//                    u.start_date = null;
                    // TODO update local user hash with correct start_date
                    assert (UserController.USER_MAP.get(user_id).device_id == null);
                }
                UserController.requestUsers();
                DeviceController.requestDevices();
                return true;
            }
            return false;
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }



    // DELETE a DEVICE from a USER
    public static boolean checkinDevice(String user_id, String device_id) {
        Log.d("checkinDevice", "");
        if ((user_id == null) || (user_id.length() < 1)) {
            return false;
        }
        if ((device_id == null) || (device_id.length() < 1)) {
            return false;
        }
        HttpUrl url = HttpUrl.parse(_getCheckoutUrlString(user_id, device_id));
        Request request = new Request.Builder().delete().url(url).build();
        OkHttpClient okHttp = client.getOkHttpClient();

        try{
            Response response = okHttp.newCall(request).execute();
            // verify device was deleted from user
            if (response.code() == 204) {
                // Success
                // Update User Info
                // TODO check user is saved in
                int i = UserController.findIndexById(user_id);
                if (i != -1) {
                    User u = UserController.users.get(i);
                    u.device_id = null;
                    u.start_date = null;
                    assert (UserController.users.get(i).device_id == null);
                    assert (UserController.users.get(i).start_date == null);
                }
                User u = UserController.USER_MAP.get(user_id);
                if (u != null) {
                    u.device_id = null;
                    u.start_date = null;
                    assert (UserController.USER_MAP.get(user_id).device_id == null);
                    assert (UserController.USER_MAP.get(user_id).start_date == null);
                }
                return true;
            }
            else {
                // Failed to delete
                return false;
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            return false;
        }

    }
}
