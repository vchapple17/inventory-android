package com.example.valchapple.hybrid_android.models;


/**
 * Created by valchapple on 3/6/18.
 * Class Model to use within app
 */

public class User {

    public final String id;
    public String first_name;
    public String family_name;
    public String group;
    public String device_id;
    public String start_date;

    public User(String id, String first_name, String family_name, String group, String device_id, String start_date) {
        this.id = id;
        this.first_name = first_name;
        this.family_name = family_name;
        this.group = group;
        this.device_id = device_id;
        this.start_date = start_date;
    }

    public String getFullName() {
        return this.first_name + " " + this.family_name;
    }

    public String getFullNameRev() {
        return this.family_name + ", " + this.first_name;
    }

    @Override
    public String toString() {
        return this.getFullNameRev();
    }
}