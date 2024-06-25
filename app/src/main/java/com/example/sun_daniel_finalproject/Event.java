package com.example.sun_daniel_finalproject;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Event {

    private String title;
    private String date;
    private String location;
    private String description;
    private double latitude;
    private double longitude;

    // default constructor for firestore
    public Event() {}

    public Event(String title, String date, String location, String description) {
        this.title = title;
        this.date = date;
        this.location = location;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getLatitude(Context context) {
        geocodeLocation(context);
        return latitude;
    }

    public Double getLongitude(Context context) {
        geocodeLocation(context);
        return longitude;
    }

    private void geocodeLocation(Context context) {
        if (context != null && location != null && !location.isEmpty()) {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocationName(location, 1);
                if (addresses != null && !addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    this.latitude = address.getLatitude();
                    this.longitude = address.getLongitude();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
