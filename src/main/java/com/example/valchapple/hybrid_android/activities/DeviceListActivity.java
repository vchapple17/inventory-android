package com.example.valchapple.hybrid_android.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.valchapple.hybrid_android.fragments.DeviceDetailFragment;
import com.example.valchapple.hybrid_android.R;
import com.example.valchapple.hybrid_android.models.Device;
import com.example.valchapple.hybrid_android.models.MyHttpClient;

import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;

/**
 * An activity representing a list of Devices. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link DeviceDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class DeviceListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

//        HttpUrl reqUrl = HttpUrl.parse("https://hybrid-project-20180223.appspot.com/devices");
////        reqUrl = reqUrl.newBuilder().addQueryParameter("key", "AIzaSyDsx70aHdYtjvCMIDHtlK-Ni3Qf--fwURg").build();
//        MyHttpClient client = (MyHttpClient)getApplicationContext();
//        Device.requestDevices(client, reqUrl);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DeviceListActivity.this, DeviceDetailEditActivity.class);
                startActivity(intent);

            }
        });

        if (findViewById(R.id.device_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.device_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new DeviceRecyclerViewAdapter(this, Device.getDevices(), mTwoPane));
    }


    public static class DeviceRecyclerViewAdapter
            extends RecyclerView.Adapter<DeviceRecyclerViewAdapter.ViewHolder> {

        private DeviceListActivity mParentActivity;
        private List<Device> mValues = new ArrayList<>();
        private boolean mTwoPane;

        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Device device = (Device) view.getTag();
                Context context = view.getContext();
                Intent intent = new Intent(context, DeviceDetailActivity.class);
                intent.putExtra(DeviceDetailFragment.ARG_DEVICE_ID, device.id);
                context.startActivity(intent);

//                if (mTwoPane) {
//                    Bundle arguments = new Bundle();
//                    arguments.putString(DeviceDetailFragment.ARG_DEVICE_ID, device.id);
//                    DeviceDetailFragment fragment = new DeviceDetailFragment();
//                    fragment.setArguments(arguments);
//                    mParentActivity.getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.device_detail_container, fragment)
//                            .commit();
//                } else {
//                    Context context = view.getContext();
//                    Intent intent = new Intent(context, DeviceDetailActivity.class);
//                    intent.putExtra(DeviceDetailFragment.ARG_DEVICE_ID, device.id);
//                    context.startActivity(intent);
//                }
            }
        };

        DeviceRecyclerViewAdapter(DeviceListActivity parent,
                                      List<Device> devices,
                                      boolean twoPane) {
            mValues = devices;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public DeviceRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.device_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mModelView.setText(mValues.get(position).getModelText());
            holder.mSerialView.setText(mValues.get(position).getSerialText());
            holder.mStatusView.setText(mValues.get(position).getStatusText());

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mModelView;
            final TextView mSerialView;
            final TextView mStatusView;

            ViewHolder(View view) {
                super(view);
                mModelView = (TextView) view.findViewById(R.id.model_text);
                mSerialView = (TextView) view.findViewById(R.id.serial_text);
                mStatusView = (TextView) view.findViewById(R.id.status_text);
            }
        }
    }
}
