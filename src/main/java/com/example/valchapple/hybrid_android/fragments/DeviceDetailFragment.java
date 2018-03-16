package com.example.valchapple.hybrid_android.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.valchapple.hybrid_android.R;
import com.example.valchapple.hybrid_android.activities.DeviceDetailActivity;
import com.example.valchapple.hybrid_android.activities.DeviceDetailEditActivity;
import com.example.valchapple.hybrid_android.activities.DeviceListActivity;
import com.example.valchapple.hybrid_android.controller.DeviceController;
import com.example.valchapple.hybrid_android.models.Device;

import static android.app.Activity.RESULT_OK;

/**
 * A fragment representing a single Device detail screen.
 * This fragment is either contained in a {@link DeviceListActivity}
 * in two-pane mode (on tablets) or a {@link DeviceDetailActivity}
 * on handsets.
 */
public class DeviceDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_DEVICE_ID = "device_id";


    /**
     * The content this fragment is presenting.
     */
    private Device mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DeviceDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_DEVICE_ID)) {
            mItem = DeviceController.DEVICE_MAP.get(getArguments().getString(ARG_DEVICE_ID));

            if (mItem == null) {
                Log.d("DeviceDetailFragment", "mItem null");
                getActivity().finish();
                return;
            }
            else {
                Log.d("DeviceDetailFragment", mItem.toString());
            }
            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.model);
            }
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if (getArguments().containsKey(ARG_DEVICE_ID)) {
            mItem = DeviceController.DEVICE_MAP.get(getArguments().getString(ARG_DEVICE_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.model);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.device_detail, container, false);

        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.detail_device_serial)).setText(mItem.getSerialText());
            ((TextView) rootView.findViewById(R.id.detail_device_color)).setText(mItem.getColorText());
            ((TextView) rootView.findViewById(R.id.detail_device_status)).setText(mItem.getStatusText());
        }

        // Add Edit button
        Button edit_btn = rootView.findViewById(R.id.detail_device_edit_button);
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity context = getActivity();
                Intent intent = new Intent(context, DeviceDetailEditActivity.class);
                intent.putExtra(ARG_DEVICE_ID, getArguments().getString(ARG_DEVICE_ID));
                context.startActivityForResult(intent, DeviceDetailActivity.EDIT_REQUEST);
            }
        });

        // Add Delete Button
        Button delete_btn = rootView.findViewById(R.id.detail_device_delete_button);
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Delete Device and Return
                boolean result = DeviceController.deleteDevice(getArguments().getString(ARG_DEVICE_ID));

                if (result == true) {
                    Snackbar.make(view, "Device deleted.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    getActivity().setResult(RESULT_OK);
                    getActivity().finish();
                }
                else {
                    Snackbar.make(view, "Device not deleted.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        return rootView;
    }

}
