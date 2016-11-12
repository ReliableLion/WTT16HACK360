package com.example.matti.myapplication;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by alessandromorelli on 12/11/16.
 */

public class MyLocationListener implements LocationListener {
    @Override
    public void onLocationChanged(Location loc) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }
}
