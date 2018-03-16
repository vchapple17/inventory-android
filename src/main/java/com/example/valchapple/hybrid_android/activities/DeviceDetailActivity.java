package com.example.valchapple.hybrid_android.activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;

import com.example.valchapple.hybrid_android.fragments.DeviceDetailFragment;
import com.example.valchapple.hybrid_android.R;

/**
 * An activity representing a single Device detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link DeviceListActivity}.
 */
public class DeviceDetailActivity extends AppCompatActivity {

    public static final int EDIT_REQUEST = 1;
    public static final int NEW_REQUEST = 2;
    public static final int VIEW_REQUEST = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(com.example.valchapple.hybrid_android.fragments.DeviceDetailFragment.ARG_DEVICE_ID,
                    getIntent().getStringExtra(com.example.valchapple.hybrid_android.fragments.DeviceDetailFragment.ARG_DEVICE_ID));
            com.example.valchapple.hybrid_android.fragments.DeviceDetailFragment fragment = new com.example.valchapple.hybrid_android.fragments.DeviceDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.device_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public void onResumeFragments() {
        super.onResumeFragments();
        android.support.v4.app.Fragment currentFragment = this.getSupportFragmentManager().findFragmentById(R.id.device_detail_container);
        if (currentFragment instanceof DeviceDetailFragment) {
            android.support.v4.app.FragmentTransaction fragTransaction =   this.getSupportFragmentManager().beginTransaction();
            fragTransaction.detach(currentFragment);
            fragTransaction.attach(currentFragment);
            fragTransaction.commit();
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_REQUEST) {
            switch (resultCode) {
                case RESULT_OK:
                    Log.d("EDIT_REQUEST", "DeviceDetailActivity");
                    // update view
                    onResumeFragments();
                    break;
                default:
                    return;
            }
        } else if (requestCode == NEW_REQUEST) {
            switch (resultCode) {
                case RESULT_OK:
                    // update view
                    Log.d("NEW_REQUEST", "DeviceDetailActivity");
                    onResumeFragments();
                    break;
                default:
                    return;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, DeviceListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
