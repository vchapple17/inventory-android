//package com.example.valchapple.hybrid_android.helpers;
//
//import android.os.AsyncTask;
//
//// https://de.wikihow.com/In-Android-HTTP-POST-Requests-ausf%C3%BChren
//// https://developer.android.com/reference/java/net/HttpURLConnection.html
//// https://stackoverflow.com/questions/10116961/can-you-explain-the-httpurlconnection-connection-process
//// https://stackoverflow.com/questions/24399294/android-asynctask-to-make-an-http-get-request
//
//import org.json.JSONObject;
//
//import java.io.BufferedInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.ProtocolException;
//import java.net.URL;
//
///**
// * Created by valchapple on 3/6/18.
// */
//
//// https://developer.android.com/reference/android/os/AsyncTask.html
//
//class GetStringTask extends AsyncTask<URL, Void, String> {
//
//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//    }
//
//
//    public interface GetStringTaskResponse {
//        void processFinish(JSONObject output);
//    }
//
//    @Override
//    protected String doInBackground(URL... urls) {
//        HttpURLConnection httpget;
//        String jsonString = "";
//        try {
//            httpget = (HttpURLConnection) urls[0].openConnection();
//            httpget.setRequestMethod("GET");
//            InputStream in = null;
//            int statusCode = httpget.getResponseCode();
//
//            if (statusCode >= 200 && statusCode < 400) {
//                // Create an InputStream in order to extract the response objec
//                in = new BufferedInputStream(httpget.getInputStream());
//            }
//            else {
//                in = new BufferedInputStream(httpget.getErrorStream());
//            }
//            if (isCancelled()) {
//                return "";
//            }
//
//            jsonString = readStream(in);
//            // Escape early if cancel() is called
//
//        } catch (ProtocolException e1) {
//            e1.printStackTrace();
//            return "";
//        } catch (IOException e1) {
//            e1.printStackTrace();
//            return "";
//        }
//        if (httpget != null) {
//            httpget.disconnect();
//            return jsonString;
//        }
//        return "";
//    }
//
//
//
////            int status = response.getStatusLine().getStatusCode();
////
////            if (status == 200) {
////                HttpEntity entity = response.getEntity();
////                String data = EntityUtils.toString(entity);
////
////
////                JSONObject jsono = new JSONObject(data);
////
////                return true;
////            }
////
////
////        } catch (IOException e) {
////            e.printStackTrace();
////        } catch (JSONException e) {
////
////            e.printStackTrace();
////        }
////        return false;
//
//    private String readStream(InputStream in) {
//        try {
//            ByteArrayOutputStream bo = new ByteArrayOutputStream();
//            int i = in.read();
//            while (i != -1) {
//                bo.write(i);
//                i = in.read();
//            }
//            return bo.toString();
//        } catch (IOException e) {
//            return "";
//        }
//    }
//
//    @Override
//    protected void onPostExecute(String result) {
////        delegate.
////        https://stackoverflow.com/questions/12575068/how-to-get-the-result-of-onpostexecute-to-main-activity-because-asynctask-is-a
//    }
//}