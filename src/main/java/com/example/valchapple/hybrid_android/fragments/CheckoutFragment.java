package com.example.valchapple.hybrid_android.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.valchapple.hybrid_android.R;
import com.example.valchapple.hybrid_android.activities.DeviceDetailActivity;
import com.example.valchapple.hybrid_android.activities.DeviceListActivity;
import com.example.valchapple.hybrid_android.controller.DeviceController;
import com.example.valchapple.hybrid_android.controller.UserController;
import com.example.valchapple.hybrid_android.models.User;

import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * A fragment representing a single Device detail screen.
 * This fragment is either contained in a {@link DeviceListActivity}
 * in two-pane mode (on tablets) or a {@link DeviceDetailActivity}
 * on handsets.
 */
public class CheckoutFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_USER_ID = "user_id";


    /**
     * The content this fragment is presenting.
     */
    private User        mItem;
    private Spinner     mDeviceSpinner;
    private Button      mSaveButton;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CheckoutFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_USER_ID)) {
            mItem = UserController.USER_MAP.get(getArguments().getString(ARG_USER_ID));

            if (mItem == null) {
                Log.d("CheckoutFragment", "mItem null");
                getActivity().finish();
                return;
            }
            else {
                Log.d("CheckoutFragment", mItem.toString());
            }
            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getFullName());
            }
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if (getArguments().containsKey(ARG_USER_ID)) {
            mItem = UserController.USER_MAP.get(getArguments().getString(ARG_USER_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getFullName());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.checkout_content, container, false);

        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.group_text)).setText(mItem.group);
        }

        // Spinner Values
        mDeviceSpinner = rootView.findViewById(R.id.checkout_device_spinner);
        List<String> devices = DeviceController.getAvailableDeviceModelSerial();
        ArrayAdapter<String> deviceAdapter =
                new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item, devices);
        deviceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mDeviceSpinner.setAdapter(deviceAdapter);

        // Add Check Out button
        mSaveButton = rootView.findViewById(R.id.device_checkout_button);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("DEVICE CHECKOUT", "checkout button");
                Log.d("mSaveButton Device", ".setOnClickListener");
                if (checkoutDeviceSuccess()) {
                    // Save Success
                    int duration = Toast.LENGTH_SHORT;
                    CharSequence text = "Saved";
                    Toast toast = Toast.makeText(getContext(), text, duration);
                    toast.show();
                    Intent returnIntent = new Intent();
                    getActivity().setResult(RESULT_OK,returnIntent);
                    getActivity().finish();
                }
                else {
                    // Save Failed
                    int duration = Toast.LENGTH_SHORT;
                    CharSequence text = "Failed to save";
                    Toast toast = Toast.makeText(getContext(), text, duration);
                    toast.show();
                    getActivity().setResult(RESULT_CANCELED);
                }
            }
        });

        return rootView;
    }

    private boolean checkoutDeviceSuccess() {
        String device;
        Log.d("checkoutDeviceSuccess", "patch");
        // Collect Data From Form
        try {
            device = mDeviceSpinner.getSelectedItem().toString();
            if (device.length() < 1) {
                return false;
            }
            String device_id = DeviceController.getIdFromModelSerialString(device);
            if (device_id == null) {
                return false;
            }
            return mItem.checkoutDevice(device_id);
        }
        catch (NullPointerException e) {
            return false;
        }
    }

}
