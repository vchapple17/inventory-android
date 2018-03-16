package com.example.valchapple.hybrid_android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.example.valchapple.hybrid_android.R;
import com.example.valchapple.hybrid_android.fragments.CheckoutFragment;


/**
 * An activity representing a edit screen for checking out a device.
 */
public class CheckoutActivity extends AppCompatActivity {

    public static final int CHECK_OUT = 4;
    public static final int CHECK_IN = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
//        setSupportActionBar(toolbar);

//        // Show the Up button in the action bar.
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }

        // savedInstance for rotation of fragment
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(CheckoutFragment.ARG_USER_ID,
                    getIntent().getStringExtra(CheckoutFragment.ARG_USER_ID));
            CheckoutFragment fragment = new CheckoutFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.checkout_container, fragment)
                    .commit();
        }
    }

    @Override
    public void onResumeFragments() {
        super.onResumeFragments();
        android.support.v4.app.Fragment currentFragment = this.getSupportFragmentManager().findFragmentById(R.id.checkout_container);
        if (currentFragment instanceof CheckoutFragment) {
            android.support.v4.app.FragmentTransaction fragTransaction =   this.getSupportFragmentManager().beginTransaction();
            fragTransaction.detach(currentFragment);
            fragTransaction.attach(currentFragment);
            fragTransaction.commit();
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHECK_OUT) {
            switch (resultCode) {
                case RESULT_OK:
                    Log.d("CHECK_OUT", "CheckOutActivity");
                    // update view
                    onResumeFragments();
                    break;
                default:
                    return;
            }
//        } else if (requestCode == NEW_REQUEST) {
//            switch (resultCode) {
//                case RESULT_OK:
//                    // update view
//                    Log.d("NEW_REQUEST", "CheckoutActivity");
//                    onResumeFragments();
//                    break;
//                default:
//                    return;
//            }
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
            NavUtils.navigateUpTo(this, new Intent(this, CheckoutActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
