package edu.msu.fardiho.msutourapp;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class ActiveListener implements LocationListener {

    @Override
    public void onLocationChanged(Location location) {
        Log.i("refresh", "map");
        LatLng pos = new LatLng(location.getLatitude(),location.getLongitude());
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        //unimplemented
    }

    @Override
    public void onProviderEnabled(String s) {
        //unimplemented
    }

    @Override
    public void onProviderDisabled(String s) {
        //registerListeners();
    }
}
