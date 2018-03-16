package com.example.valchapple.hybrid_android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.valchapple.hybrid_android.R;
import com.example.valchapple.hybrid_android.controller.UserController;
import com.example.valchapple.hybrid_android.fragments.UserDetailFragment;
import com.example.valchapple.hybrid_android.models.User;

public class UserDetailEditActivity extends AppCompatActivity {

    private EditText    mFirstNameTextView;
    private EditText    mFamilyNameTextView;
    private Spinner     mGroupSpinner;
    private Button      mSaveButton;
    private String      user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail_edit);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Connect Input Views
        mFirstNameTextView = findViewById(R.id.user_save_first_input);
        mFamilyNameTextView = findViewById(R.id.user_save_family_input);
        mGroupSpinner = findViewById(R.id.user_save_group_spinner);
        mSaveButton = findViewById(R.id.user_save_button);

        // Spinner Values
        ArrayAdapter<CharSequence> groupAdapter = ArrayAdapter.createFromResource(this,
                R.array.user_group_array, android.R.layout.simple_spinner_item);
        groupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGroupSpinner.setAdapter(groupAdapter);

//         Pre-fill form if editing previous
        Intent intent = getIntent();
        try {
            if (intent.getExtras() != null) {
                user_id = intent.getStringExtra(UserDetailFragment.ARG_USER_ID);
                if (user_id != null) {
                    User d = UserController.USER_MAP.get(user_id);
                    String first_name = d.first_name;
                    String family_name = d.family_name;
                    String group = d.group;

                    // Set First Name
                    mFirstNameTextView.setText(first_name);

                    // Set Family Name
                    mFamilyNameTextView.setText(family_name);

                    // Set Model - Defaults to matching model or 0 index on spinner
                    mGroupSpinner.setSelection(getIndex(mGroupSpinner, group));
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
                Log.d("mSaveButtonUser", ".setOnClickListener");
                if (saveUserDetails()) {
                    // Save Success
                    int duration = Toast.LENGTH_SHORT;
                    CharSequence text = "Saved";
                    Toast toast = Toast.makeText(UserDetailEditActivity.this, text, duration);
                    toast.show();
                    Intent returnIntent = new Intent();
                    setResult(RESULT_OK,returnIntent);
                    finish();
                }
                else {
                    // Save Failed
                    int duration = Toast.LENGTH_SHORT;
                    CharSequence text = "Failed to save";
                    Toast toast = Toast.makeText(UserDetailEditActivity.this, text, duration);
                    toast.show();
                    setResult(RESULT_CANCELED);
                }
            }

        });
    }

    private boolean saveUserDetails() {
        String first_name;
        String family_name;
        String group;

        // Collect Data From Form
        try {
            first_name = mFirstNameTextView.getText().toString();
            if (first_name.length() < 1) {
                return false;
            }

            family_name = mFamilyNameTextView.getText().toString();
            if (family_name.length() < 1) {
                return false;
            }

            group = mGroupSpinner.getSelectedItem().toString();
            if (group.length() < 1) {
                return false;
            }

        }
        catch (NullPointerException e) {
            return false;
        }

        // If user_id is null, create new User, else PATCH
        if (this.user_id == null) {
            // Create new user
            Log.d("saveUserDetails", "post");
            return UserController.postUserDetails(first_name, family_name, group);
        }
        else {
            // Save Existing user
            Log.d("saveUserDetails", "patch");
            return UserController.patchUserDetails(user_id, first_name, family_name, group);
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
