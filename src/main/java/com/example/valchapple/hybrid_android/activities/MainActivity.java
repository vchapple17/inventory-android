package com.example.valchapple.hybrid_android.activities;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.valchapple.hybrid_android.R;
import com.example.valchapple.hybrid_android.controller.DeviceController;
import com.example.valchapple.hybrid_android.models.Device;
import com.example.valchapple.hybrid_android.models.MyHttpClient;


// https://stackoverflow.com/questions/18588532/how-should-i-pass-around-singleton-objects-to-android-activities

public class MainActivity extends AppCompatActivity {
    static String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        // INSTANCE

        DeviceController.client = (MyHttpClient)getApplicationContext();
        DeviceController.requestDevices();

        // Add buttons
        Button devicesBtn = findViewById(R.id.btn_devices);
//        Button usersBtn = findViewById(R.id.btn_users);
//        Button checkinBtn = findViewById(R.id.btn_checkin);
//        Button checkoutBtn = findViewById(R.id.btn_checkout);

        // Add click listeners
        devicesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DeviceListActivity.class);
                startActivity(intent);
            }
        });
    }
}
