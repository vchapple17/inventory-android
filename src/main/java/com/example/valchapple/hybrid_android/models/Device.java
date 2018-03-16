package com.example.valchapple.hybrid_android.models;


/**
 * Created by valchapple on 3/6/18.
 * Class Model to use within app
 */

public class Device {

    public final String id;
    public String serial;
    public String model;
    public String color;
    public boolean is_rented;

    public Device(String id, boolean is_rented, String serial, String model, String color) {
        this.id = id;
        this.serial = serial;
        this.model = model;
        this.color = color;
        this.is_rented = is_rented;
    }

    public String getModelText() {
        return model.toString();
    }

    public String getColorText() {
        return color.toString();
    }

    public String getSerialText() {
        return serial.toString();
    }

    public String getStatusText() {
        if (is_rented == true) {
            return "CHECKED OUT";
        } else {
            return "AVAILABLE";
        }
    }

    @Override
    public String toString() {
        return this.serial;
    }
}