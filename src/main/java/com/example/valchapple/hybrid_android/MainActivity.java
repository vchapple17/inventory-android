package com.example.valchapple.hybrid_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Add buttons
        Button devicesBtn = findViewById(R.id.btn_devices);
        Button usersBtn = findViewById(R.id.btn_users);
        Button checkinBtn = findViewById(R.id.btn_checkin);
        Button checkoutBtn = findViewById(R.id.btn_checkout);

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
