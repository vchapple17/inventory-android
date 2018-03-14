package com.example.valchapple.hybrid_android.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.valchapple.hybrid_android.R;
import com.example.valchapple.hybrid_android.activities.DeviceDetailEditActivity;
import com.example.valchapple.hybrid_android.activities.MainActivity;
import com.example.valchapple.hybrid_android.models.Device;
import com.example.valchapple.hybrid_android.activities.DeviceListActivity;
import com.example.valchapple.hybrid_android.activities.DeviceDetailActivity;

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
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
//            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_DEVICE_ID));
            mItem = Device.DEVICE_MAP.get(getArguments().getString(ARG_DEVICE_ID));

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
                Context context = getActivity();
                Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(context, DeviceDetailEditActivity.class);
                intent.putExtra(ARG_DEVICE_ID, getArguments().getString(ARG_DEVICE_ID));
                context.startActivity(intent);
            };
        });

        return rootView;
    }
}
