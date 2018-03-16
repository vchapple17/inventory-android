package com.example.valchapple.hybrid_android.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.valchapple.hybrid_android.R;
import com.example.valchapple.hybrid_android.controller.DeviceController;
import com.example.valchapple.hybrid_android.fragments.DeviceDetailFragment;
import com.example.valchapple.hybrid_android.models.Device;
import com.example.valchapple.hybrid_android.models.MyHttpClient;

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

        // Spinner Values
        ArrayAdapter<CharSequence> modelAdapter = ArrayAdapter.createFromResource(this,
                R.array.device_model_array, android.R.layout.simple_spinner_item);
        modelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mModelSpinner.setAdapter(modelAdapter);

        ArrayAdapter<CharSequence> colorAdapter = ArrayAdapter.createFromResource(this,
                R.array.device_color_array, android.R.layout.simple_spinner_item);
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mColorSpinner.setAdapter(colorAdapter);

//         Pre-fill form if editing previous
        Intent intent = getIntent();
        try {
            if (intent.getExtras() != null) {
                device_id = intent.getStringExtra(DeviceDetailFragment.ARG_DEVICE_ID);
                if (device_id != null) {
                    Device d = DeviceController.DEVICE_MAP.get(device_id);
                    String device_serial = d.getSerialText();
                    String device_model = d.getModelText();
                    String device_color = d.getColorText();

                    // Set Serial Number
                    mSerialTextView.setText(device_serial);

                    // Set Model - Defaults to matching model or 0 index on spinner
                    mModelSpinner.setSelection(getIndex(mModelSpinner, device_model));

                    // Set Color - Defaults to color matched or 0 index on spinner
                    mColorSpinner.setSelection(getIndex(mColorSpinner, device_color));
                }
            }
        } catch(NullPointerException e) {
            e.printStackTrace();
            setResult(RESULT_CANCELED);
            finish();
        }

        mSaveButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("mSaveButton", ".setOnClickListener");
                if (saveDeviceDetails()) {
                    // Save Success
                    int duration = Toast.LENGTH_SHORT;
                    CharSequence text = "Saved";
                    Toast toast = Toast.makeText(DeviceDetailEditActivity.this, text, duration);
                    toast.show();
                    Intent returnIntent = new Intent();
//                    returnIntent.putExtra("update", true);
                    setResult(RESULT_OK,returnIntent);
                    finish();
                }
                else {
                    // Save Failed
                    int duration = Toast.LENGTH_SHORT;
                    CharSequence text = "Failed to save";
                    Toast toast = Toast.makeText(DeviceDetailEditActivity.this, text, duration);
                    toast.show();
                    setResult(RESULT_CANCELED);
//                    finish();
                }
            }

        });
    }

    private boolean saveDeviceDetails() {
        // Return 1 for successful save
        // Return 0 for failed save
        String serial;
        String model;
        String color;

        // Collect Data From Form
        try {
            serial = mSerialTextView.getText().toString();
            if (serial.length() < 1) {
                return false;
            }

            model = mModelSpinner.getSelectedItem().toString();
            if (model.length() < 1) {
                return false;
            }

            color = mColorSpinner.getSelectedItem().toString();
            if (color.length() < 1) {
                return false;
            }

        }
        catch (NullPointerException e) {
            return false;
        }

        // If device_id is null, create new Device, else PATCH
        if (this.device_id == null) {
            // Create new device
            Log.d("saveDeviceDetails", "post");
            return DeviceController.postDeviceDetails(serial, model, color);
        }
        else {
            // Save Existing device
            Log.d("saveDeviceDetails", "patch");
            return DeviceController.patchDeviceDetails(device_id, serial, model, color);
        }
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
