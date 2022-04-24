package edu.msu.fardiho.msutourapp;

import edu.msu.fardiho.msutourapp.Landmark;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import edu.msu.fardiho.msutourapp.MarkerClickListener;
import edu.msu.fardiho.msutourapp.ActiveListener;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;

import java.util.ArrayList;

public class TourActivity extends FragmentActivity implements OnMapReadyCallback {


    LocationManager locationManager = null;
    private final ActiveListener activeListener = new ActiveListener();
    private GoogleMap mMap;
    protected ArrayList<Landmark> landmarkArrayList = new ArrayList<Landmark>();
    MarkerClickListener mkcl = new MarkerClickListener(this);
    private MarkerOptions user_markops;
    private Marker userMarker;
    CreateLndMrkDlg clmdlg = new CreateLndMrkDlg();
    private double user_latitude =0;
    private double user_longitude =0;
    private String user;
    private String userId;

    private boolean getPermissionStatus() {
        return ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED;
    }
    private void obtainPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        //permissions is not granted request permissions
        if (getPermissionStatus()) {
            obtainPermission();
        }

        //set location manager
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        // Force the screen to say on and bright
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Intent i = getIntent();

        //TODO: load the user id from extras #DONE
        //load user name and userId
        if (i != null) {
            user = i.getExtras().getString("USERNAME");
            userId = i.getExtras().getString("UID");
            Log.i("username", user);
            Log.i("userId", userId);
        }
        //TODO: Load landmarks from database
        Server server = new Server();
        try {
            server.RequestToServer(user, userId, "FETCH_LANDMARKS");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        boolean success = mMap.setMapStyle(new MapStyleOptions(getResources()
                .getString(R.string.style_json)));
        if (!success) {
            Log.e("TAG", "Style parsing failed.");
        }
        //pin user on the map
        LatLng user = new LatLng(user_latitude, user_longitude);
        user_markops = new MarkerOptions()
                .position(user)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.userpin));
        userMarker = mMap.addMarker(user_markops);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(user, 17.5f));
        //pin all the landmarks
        for (Landmark l : landmarkArrayList) {
            pinLandmark(l);
        }
        mMap.setOnMarkerClickListener(mkcl);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerListeners();
    }

    @Override
    protected void onPause() {
        unregisterListeners();
        super.onPause();
    }

    public void onDlgCancel(View view){
        clmdlg.dismiss();
    }

    //bring up create landmark dialog
    public void onCreateLandmark(View view) {
        clmdlg.show(getSupportFragmentManager(), "create");
        clmdlg.setTourActivity(this);
    }
    //load landmarks from sql server and stores them into the array

    //place landmark on the map
    public void pinLandmark(Landmark lm) {
        mMap.addMarker(lm.getMarkOps());
    }

    private void registerListeners(){
        //unregister any listeners
        unregisterListeners();
        //set criteria for new location provider
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        criteria.setAltitudeRequired(true);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(false);
        //get the provider
        String best = locationManager.getBestProvider(criteria, true);
        //if best provider is available check permissions,
        //set periodic location updates, and locate the user
        if (best != null){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                return;
            }
            locationManager.requestLocationUpdates(best, 200, 0, activeListener);
            Location location = locationManager.getLastKnownLocation(best);
            if (location != null){
               setUserLocation(location);
            }
        }
    }

    private void unregisterListeners() {
        locationManager.removeUpdates(activeListener);
    }

    //setters
    public void setUserLocation(Location location) {
        user_latitude = location.getLatitude();
        user_longitude = location.getLongitude();
    }

    //getters
    public Location getUserlocation() { return new Location(LocationManager.GPS_PROVIDER); }

    public double getUserLongitude(){
        return user_longitude;
    }

    public double getUserLatitude() {
        return user_latitude;
    }

    public ArrayList<Landmark> getLandmarkArrayList() { return landmarkArrayList; }

    //Notifiers and Refreshers
    public void refreshMap() {
        Log.i("array", landmarkArrayList.toString());
        if (mMap != null) {
            mMap.clear();
            onMapReady(mMap);
        }
    }

    public void notifyUser(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT)
                .show();
    }

}
