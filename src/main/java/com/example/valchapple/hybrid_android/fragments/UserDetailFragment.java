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
import android.widget.Toast;

import com.example.valchapple.hybrid_android.R;
import com.example.valchapple.hybrid_android.activities.CheckoutActivity;
import com.example.valchapple.hybrid_android.activities.UserDetailActivity;
import com.example.valchapple.hybrid_android.activities.UserDetailEditActivity;
import com.example.valchapple.hybrid_android.activities.UserListActivity;
import com.example.valchapple.hybrid_android.controller.DeviceController;
import com.example.valchapple.hybrid_android.controller.UserController;
import com.example.valchapple.hybrid_android.models.User;

import static android.app.Activity.RESULT_OK;

/**
 * A fragment representing a single User detail screen.
 * This fragment is either contained in a {@link UserListActivity}
 * in two-pane mode (on tablets) or a {@link UserDetailActivity}
 * on handsets.
 */
public class UserDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_USER_ID = "user_id";


    /**
     * The content this fragment is presenting.
     */
    private User mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public UserDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_USER_ID)) {
            mItem = UserController.USER_MAP.get(getArguments().getString(ARG_USER_ID));

            if (mItem == null) {
                Log.d("UserDetailFragment", "mItem null");
                getActivity().finish();
                return;
            }
            else {
                Log.d("UserDetailFragment", mItem.toString());
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
        final View rootView = inflater.inflate(R.layout.user_detail, container, false);

        // Setup Detail as long as item is not null
        if (mItem != null) {

            // DETAILS
            boolean hasDevice = true;
            String serial = DeviceController.getSerialById(mItem.device_id);
            if (serial == null) {
                hasDevice = false;
                serial = getString(R.string.user_no_device);
            }
            ((TextView) rootView.findViewById(R.id.detail_user_device_serial)).setText(serial);
            ((TextView) rootView.findViewById(R.id.detail_user_first)).setText(mItem.first_name);
            ((TextView) rootView.findViewById(R.id.detail_user_family)).setText(mItem.family_name);
            ((TextView) rootView.findViewById(R.id.detail_user_group)).setText(mItem.group);
            if (mItem.start_date != null) {
                ((TextView) rootView.findViewById(R.id.detail_user_device_date)).setText(mItem.start_date);
            } else {
                ((TextView) rootView.findViewById(R.id.detail_user_device_date)).setText("");
            }

            // BUTTONS
            Button rent_btn = rootView.findViewById(R.id.detail_user_checkout_button);
            if (hasDevice) {
                // CHECK IN DEVICE
                rent_btn.setText(getString(R.string.btn_checkin));
                rent_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Activity context = getActivity();
                        Log.d("CHECK-IN BUTTON", "CHECK-IN DEVICE");
                    }
                });
            } else {
                // CHECK OUT DEVICE
                rent_btn.setText(getString(R.string.btn_checkout));
                rent_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Activity context = getActivity();
                        Intent intent = new Intent(context, CheckoutActivity.class);
                        intent.putExtra(ARG_USER_ID, getArguments().getString(ARG_USER_ID));
                        context.startActivityForResult(intent, CheckoutActivity.CHECK_OUT);
                    }
                });
            }

            // Add Edit button
            Button edit_btn = rootView.findViewById(R.id.detail_user_edit_button);
            edit_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Activity context = getActivity();
                    Intent intent = new Intent(context, UserDetailEditActivity.class);
                    intent.putExtra(ARG_USER_ID, getArguments().getString(ARG_USER_ID));
                    context.startActivityForResult(intent, UserDetailActivity.EDIT_REQUEST);
                }
            });

            // Add Delete Button
            Button delete_btn = rootView.findViewById(R.id.detail_user_delete_button);
            if (hasDevice) {
                delete_btn.setBackgroundColor(getResources()
                        .getColor(R.color.colorDisabled, getContext().getTheme()));
                delete_btn.setTextColor(getResources()
                        .getColor(R.color.colorTextDisabled, getContext().getTheme()));
                delete_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int duration = Toast.LENGTH_SHORT;
                        CharSequence text = "Must return device first.";
                        Toast toast = Toast.makeText(getContext(), text, duration);
                        toast.show();
                    }
                });
            } else {
                delete_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Delete User and Return
                        boolean result = UserController.deleteUser(getArguments().getString(ARG_USER_ID));

                        if (result == true) {
                            Snackbar.make(view, "User deleted.", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            getActivity().setResult(RESULT_OK);
                            getActivity().finish();
                        }
                        else {
                            Snackbar.make(view, "User not deleted.", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }
                });
            }
        }
        return rootView;
    }

}
