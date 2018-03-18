package com.example.valchapple.hybrid_android.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.valchapple.hybrid_android.R;
import com.example.valchapple.hybrid_android.controller.DeviceController;
import com.example.valchapple.hybrid_android.controller.UserController;
import com.example.valchapple.hybrid_android.fragments.UserDetailFragment;
import com.example.valchapple.hybrid_android.models.User;

import java.util.ArrayList;
import java.util.List;


/**
 * An activity representing a list of Users.
 */
public class UserListActivity extends AppCompatActivity {
    private UserRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserListActivity.this, UserDetailEditActivity.class);
                startActivityForResult(intent, UserDetailActivity.NEW_REQUEST);
            }
        });

        View recyclerView = findViewById(R.id.user_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == UserDetailActivity.NEW_REQUEST) {
            switch (resultCode) {
                case RESULT_OK:
                    // update view
                    mAdapter.notifyDataSetChanged();
                    break;
                default:
                    return;
            }
        }
        if (requestCode == UserDetailActivity.VIEW_REQUEST) {
            switch (resultCode) {
                case RESULT_OK:
                    // update view
                    Log.d("UserListActivity", "VIEW_REQUEST OK");
                    mAdapter.notifyDataSetChanged();

                    break;
                default:
                    return;
            }
        }

        return;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        mAdapter = new UserRecyclerViewAdapter(this, UserController.getUsers());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
    }

    public static class UserRecyclerViewAdapter
            extends RecyclerView.Adapter<UserRecyclerViewAdapter.ViewHolder> {

        private UserListActivity mParentActivity;
        private List<User> mValues = new ArrayList<>();

        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = (User) view.getTag();
                Context context = view.getContext();
                Intent intent = new Intent(context, UserDetailActivity.class);
                intent.putExtra(UserDetailFragment.ARG_USER_ID, user.id);
                mParentActivity.startActivityForResult(intent, UserDetailActivity.VIEW_REQUEST);
            }
        };

        UserRecyclerViewAdapter(UserListActivity parent,
                                  List<User> users ) {
            mValues = users;
            mParentActivity = parent;
        }

        @Override
        public UserRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.user_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
                holder.mFamilyNameView.setText(mValues.get(position).family_name);
                holder.mFirstNameView.setText(mValues.get(position).first_name);
                holder.mGroupView.setText(mValues.get(position).group);

            // Get Device Serial
                String device_serial = DeviceController.getSerialById( mValues.get(position).device_id);
                if (device_serial != null) {
                    holder.mDeviceSerialView.setText(device_serial);
                }
                holder.itemView.setTag(mValues.get(position));
                holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mFamilyNameView;
            final TextView mFirstNameView;
            final TextView mGroupView;
            final TextView mDeviceSerialView;

            ViewHolder(View view) {
                super(view);
                mFamilyNameView = view.findViewById(R.id.family_name_text);
                mFirstNameView = view.findViewById(R.id.first_name_text);
                mGroupView = view.findViewById(R.id.group_text);
                mDeviceSerialView = view.findViewById(R.id.user_device_serial_text);

            }
        }
    }
}
