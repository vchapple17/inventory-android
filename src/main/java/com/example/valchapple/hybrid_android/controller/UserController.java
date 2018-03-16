package com.example.valchapple.hybrid_android.controller;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.valchapple.hybrid_android.models.MyHttpClient;
import com.example.valchapple.hybrid_android.models.User;

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
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserController extends AppCompatActivity {

    public static final Map<String, User> USER_MAP= new HashMap<String, User>();
    public static List<User> users = new ArrayList<>();
    public static boolean isUpdating = false;
    public static MyHttpClient client;
    public static final int SORT_FAMILY = 0;
    public static final int SORT_FIRST = 1;
    public static final int SORT_GROUP = 2;
    public static int sort_mode = SORT_FAMILY;  // By Family is default

    // STATIC HELPERS
    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String usersURL = "https://hybrid-project-20180223.appspot.com/users";

    public static User readJSONUser(JSONObject user_JSON) throws JSONException {
        Log.d("readJSONUser", user_JSON.toString());
        String id = user_JSON.getString("id");
        String first_name = user_JSON.getString("first_name");
        String family_name = user_JSON.getString("family_name");
        String group = user_JSON.getString("group");
        String device_id = user_JSON.getString("device_id");
        String start_datetime = user_JSON.getString("start_datetime");

        return new User(id, first_name, family_name, group, device_id, start_datetime);
    }
    private static String _makePostString(String first_name, String family_name, String group) {
        JSONObject d = new JSONObject();
        try {
            d.put("first_name", first_name);
            d.put("family_name", family_name);
            d.put("group", group);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return d.toString();
    }
    private static String _makePatchString(String first_name, String family_name, String group) {
        JSONObject d = new JSONObject();
        try {
            if (first_name != null) {
                d.put("first_name", first_name);
            }
            if (family_name != null) {
                d.put("family_name", family_name);
            }
            if (group != null) {
                d.put("group", group);
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
    private static String _getUserUrlString(String user_id) {
        return usersURL + "/" + user_id;
    }

    public static List<User> getUsers() {
        // Ascending Order
        sortUsers();
        return users;
    }

    public static void sortUsers() {
        switch (sort_mode) {
            case SORT_FAMILY:
                sortUsersByFamilyName();
                break;
            case SORT_FIRST:
                sortUsersByFirstName();
                break;
            case SORT_GROUP:
                sortUsersByGroup();
                break;
            default:
                sortUsersByFamilyName();
        }
    }
    public static void sortUsersByFamilyName() {
        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
//                String str1 = o1.family_name;
//                String str2 = o2.family_name;
                String str1 = o1.getFullNameRev();
                String str2 = o2.getFullNameRev();
                int res = String.CASE_INSENSITIVE_ORDER.compare(str1, str2);
                if (res == 0) {
                    res = str1.compareTo(str2);
                }
                return res;
            }
        });
    }
    public static void sortUsersByFirstName() {
        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                String str1 = o1.first_name;
                String str2 = o2.first_name;
                int res = String.CASE_INSENSITIVE_ORDER.compare(str1, str2);
                if (res == 0) {
                    res = str1.compareTo(str2);
                }
                return res;
            }
        });
    }
    public static void sortUsersByGroup() {
        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                String str1 = o1.group;
                String str2 = o2.group;
                int res = String.CASE_INSENSITIVE_ORDER.compare(str1, str2);
                if (res == 0) {
                    res = str1.compareTo(str2);
                }
                return res;
            }
        });
    }

    // GET Users Request
    public static void requestUsers() {
        isUpdating = true;
        HttpUrl url = HttpUrl.parse(usersURL);
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
                USER_MAP.clear();
                users.clear();
                try {
                    JSONArray user_arr = new JSONArray(r);
                    User d;
                    for(int i = 0; i < user_arr.length(); i++){
                        // Parse into User
                        d = readJSONUser(user_arr.getJSONObject(i));
                        USER_MAP.put(d.id, d);
                        users.add(d);
                    }
                    sortUsers();
                    isUpdating = false;
                } catch (JSONException e) {
                    isUpdating = false;
                    e.printStackTrace();
                };
                isUpdating = false;
            }
        });

    }

    // POST User Request
    public static boolean postUserDetails(String first_name, String family_name, String group) {
        HttpUrl url = HttpUrl.parse(usersURL);
        String postString = _makePostString(first_name, family_name, group);
        if (postString == null) {
            return false;
        }

        Log.d("postUserDetails", postString);
        RequestBody reqBody = RequestBody.create(MEDIA_TYPE_JSON, postString );

        Request request = new Request.Builder().post(reqBody).url(url).build();
        OkHttpClient okHttp = client.getOkHttpClient();

        try{
            Response response = okHttp.newCall(request).execute();
            String r = response.body().string();
            JSONObject user_obj = new JSONObject(r);
            User d = readJSONUser(user_obj);;
            USER_MAP.put(d.id, d);
            users.add(d);
            sortUsers();
//            requestUsers();
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

    // PATCH User Request
    public static boolean patchUserDetails(String user_id, String first_name, String family_name, String group) {
        if ((user_id == null) || (user_id.length() < 1)) {
            return false;
        }
        String patchString = _makePatchString(first_name, family_name, group);
        if (patchString == null) {
            return false;
        }

        String user_string = _getUserUrlString(user_id);
        HttpUrl url = HttpUrl.parse(user_string);

        RequestBody reqBody = RequestBody.create(MEDIA_TYPE_JSON, patchString);
        Request request = new Request.Builder().patch(reqBody).url(url).build();
        OkHttpClient okHttp = client.getOkHttpClient();

        try{
            Response response = okHttp.newCall(request).execute();
            String r = response.body().string();
            JSONObject user_obj = new JSONObject(r);
            User d = readJSONUser(user_obj);;
            USER_MAP.replace(d.id, d);
            int i = findIndexById(d.id);
            if (i == -1) {
                return false;
            }
            users.set(findIndexById(d.id), d);
            sortUsers();
            return true;
        } catch (IOException | NullPointerException | JSONException e) {
            e.printStackTrace();
            return false;
        }

    }

    // DELETE User Request
    public static boolean deleteUser(String user_id) {
        if ((user_id == null) || (user_id.length() < 1)) {
            return false;
        }

        String user_string = _getUserUrlString(user_id);
        HttpUrl url = HttpUrl.parse(user_string);

        Request request = new Request.Builder().delete().url(url).build();
        OkHttpClient okHttp = client.getOkHttpClient();

        try{
            Response response = okHttp.newCall(request).execute();
            // verify user was deleted
            if (response.code() == 204) {
                // Success
                USER_MAP.remove(user_id);
                int ind = findIndexById(user_id);
                if (ind != -1) {
                    users.remove(ind);
                }
                requestUsers();
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

    public static int findIndexById(String user_id) {
        int i = 0;
        for (i = 0; i < users.size(); i++ ) {
            if (users.get(i).id.equals(user_id)) {
                return i;
            }
        }
        return -1;
    }
}
