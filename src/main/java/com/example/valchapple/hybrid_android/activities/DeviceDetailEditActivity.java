package com.example.valchapple.hybrid_android.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.valchapple.hybrid_android.R;

public class DeviceDetailEditActivity extends AppCompatActivity {

    private EditText    mSerialTextView;
    private Spinner     mModelSpinner;
    private Spinner     mColorSpinner;
    private Button      mSaveButton;
    private String      device_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_detail_edit);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Connect Input Views
        mSerialTextView = findViewById(R.id.device_save_serial_input);
        mModelSpinner = findViewById(R.id.device_save_model_spinner);
        mColorSpinner = findViewById(R.id.device_save_color_spinner);
        mSaveButton = findViewById(R.id.device_save_button);

        // Put Data To Fill
        Intent intent = getIntent();
        device_id = intent.getStringExtra("device_id");
        // Pre-fill form if editing previous
        if (device_id != null) {
            String device_serial = intent.getStringExtra("device_serial");
            String device_model = intent.getStringExtra("device_model");
            String device_color = intent.getStringExtra("device_color");

            // Set Serial Number
            mSerialTextView.setText(device_serial);

            // Set Model - Defaults to matching model or 0 index on spinner
            mModelSpinner.setSelection(getIndex(mModelSpinner, device_model));

            // Set Color - Defaults to color matched or 0 index on spinner
            mColorSpinner.setSelection(getIndex(mColorSpinner, device_color));
        }

        mSaveButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saveDeviceDetails() == 1) {
                    // Save Success

                }
                else {
                    // Save Failed
                }
            }

        });



    }

    private int saveDeviceDetails() {
        // Return 1 for successful save
        // Return 0 for failed save
        String serial;
        String model;
        String color;

        // Collect Data From Form
        if (mSerialTextView != null) {
            serial = mSerialTextView.getText().toString();
            if (serial.length() < 1) {
                return 0;
            }
        }
        else {
            return 0;
        }
        if (mModelSpinner != null) {
            model = mModelSpinner.getSelectedItem().toString();
            if (model.length() < 1) {
                return 0;
            }
        }
        else {
            return 0;
        }

        if (mColorSpinner != null) {
            color = mColorSpinner.getSelectedItem().toString();
            if (color.length() < 1) {
                return 0;
            }
        }
        else {
            return 0;
        }

        // If device_id is null, create new Device, else PATCH
        if (this.device_id == null) {
            // Create new device
            return postDeviceDetails(serial, model, color);
        }
        else {
            // Save Existing device
            return patchDeviceDetails(serial, model, color);
        }
    }

    private int postDeviceDetails(String serial, String model, String color) {
        // Return 1 for successful save
        // Return 0 for failed save


        return 1;
    }

    private int patchDeviceDetails(String serial, String model, String color) {
        // Return 1 for successful save
        // Return 0 for failed save


        return 1;
    }



    private int getIndex(Spinner spinner, String str) {
        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(str)){
                index = i;
                break;
            }
        }
        return index;
    }
}
