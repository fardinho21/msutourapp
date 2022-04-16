package edu.msu.fardiho.msutourapp;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;



import edu.msu.fardiho.msutourapp.Server.Server;

public class TourActivity extends FragmentActivity implements OnMapReadyCallback {

    //TEST TIMER
    public static String magic = "NechAtHa6RuzeR8x";
    LocationManager locationManager = null;
    private ActiveListener activeListener = new ActiveListener();
    private GoogleMap mMap;
    Server server;
    protected ArrayList<Landmark> landmarkArrayList = new ArrayList<Landmark>();
    private class ActiveListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            Log.i("refresh", "map");
            refreshMap();
            //update user position marker on map
            LatLng pos = new LatLng(location.getLatitude(),location.getLongitude());
            userMarker.setPosition(pos);
            //mMap.animateCamera(CameraUpdateFactory.newLatLng(pos));
            locateUser(location);
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
            registerListeners();
        }
    }

    public class MarkerClickListener implements GoogleMap.OnMarkerClickListener{
        LndMrkDescDlg lndMrkDescDlg;
        @Override
        public boolean onMarkerClick(Marker marker) {

            if(marker.getId().equals("m0")){
                Toast.makeText(TourActivity.this, "That's you!", Toast.LENGTH_SHORT).show();
                return false;
            }
            //get landmark position
            LatLng latLng = marker.getPosition();
            Log.i("latlng", latLng.toString());
            String lndmrk_name = "Name";
            String lndmrk_desc = "Description";
            //find the landmark in the array
            for (Landmark l : landmarkArrayList){
                if ( marker.getTitle().equals(l.getName())){
                    lndmrk_name = l.getName();
                    if (l.getDesc() != null){
                        lndmrk_desc = l.getDesc();
                    }
                }
            }
            lndMrkDescDlg = new LndMrkDescDlg(lndmrk_name, lndmrk_desc);
            lndMrkDescDlg.show(getSupportFragmentManager(), "desc");
            return false;
        }
    }

    MarkerClickListener mkcl = new MarkerClickListener();
    private MarkerOptions user_markops;
    private Marker userMarker;

    public class Landmark {

        private MarkerOptions mark;
        private String desc;
        private String name;
        private float lat;
        private float lon;
        public float getLat(){return lat;}
        public float getLon(){return lon;}
        public String getDesc(){return desc;}
        public String getName() {
            return name;
        }
        public void setMarkOps(MarkerOptions m) {
            mark = m;
        }
        public MarkerOptions getMarkOps() {return mark;}
        public void setDesc(String desc){
            this.desc = desc;
        }
        public void setTitle(String title){
            this.name = title;
        }
        public void setLat(float lat){
            this.lat = lat;
        }
        public void setLon(float lon){
            this.lon = lon;
        }

        Landmark (String des, float la, float lo, String n) {
            lat = la; lon = lo; desc = des; name = n;
        }
        Landmark () {
            lat = 0; lon = 0; desc = ""; name = "";
        }
    }

    CreateLndMrkDlg clmdlg = new CreateLndMrkDlg();
    private double user_latitude =0;
    private double user_longitude =0;
    String user;
    String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //permissions is not grandted request permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        // Force the screen to say on and bright
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Intent i = getIntent();
        //load user name and password
        if (i != null) {
            user = i.getExtras().getString("USERNAME");
            pass = i.getExtras().getString("PASS");
            Log.i("username", user);
            Log.i("pass", pass);
        }
        //load all landmarks from database
        loadLandMarks();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
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

    //bring up create landmark dialog
    public void onCreateLandmark(View view) {
        clmdlg.show(getSupportFragmentManager(), "create");
        clmdlg.setTourActivity(this);
    }
    //load landmarks from sql server and stores them into the array

    public void loadLandMarks() {
        Log.i("loadLandMarks", "called");
    }

    public static void skipToEndTag(XmlPullParser xml) throws IOException, XmlPullParserException {
        int tag;
        do
        {
            tag = xml.next();
            if(tag == XmlPullParser.START_TAG) {
                // Recurse over any start tag
                skipToEndTag(xml);
            }
        } while(tag != XmlPullParser.END_TAG &&
                tag != XmlPullParser.END_DOCUMENT);
    }

    //place landmark on the map
    public void pinLandmark(Landmark lm) {
        mMap.addMarker(lm.getMarkOps());
    }

    //get lat and lon for user
    public void locateUser(Location location) {
        user_latitude = location.getLatitude();
        user_longitude = location.getLongitude();
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
               locateUser(location);
            }
        }
    }

    private void unregisterListeners() {
        locationManager.removeUpdates(activeListener);
    }

    public void onDlgCancel(View view){
        clmdlg.dismiss();
    }

    //get information from dialog, create the landmark and store it in landmark array
    //then call the sql server to store the landmark in the database table
    public void onDlgCreate(View view){
        //get entered info and dismiss
        HashMap<String, String> infoHashMap = clmdlg.getInfo();
        clmdlg.dismiss();
        Log.i("infoHashMap", infoHashMap.toString());
        //create the landmark
        String desc = infoHashMap.get("desc");
        String name = infoHashMap.get("name");

        if (infoHashMap.get("longitude")!=null && infoHashMap.get("latitude")!=null) {
            float longi = Float.valueOf(infoHashMap.get("longitude"));
            float lati = Float.valueOf(infoHashMap.get("latitude"));
            //instantiate landmark object
            Landmark lm = new Landmark(desc,lati,longi,name);
            //get location and set marker options
            LatLng lmrk_loc = new LatLng(user_latitude, user_longitude);
            MarkerOptions mrkops = new MarkerOptions()
                    .position(lmrk_loc)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.markericon));
            lm.setMarkOps(mrkops);
            landmarkArrayList.add(lm);
            Log.i("landmarkArrayList",landmarkArrayList.toString());

            //final String xmlStr = writer.toString();
            final String sendString = "?name="+name+"&latitude="+Float.toString(lati)+"&longitude="+Float.toString(longi)+"&description="+desc;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    //int res = server.createLandmark(xmlStr);
                    Server server = new Server();
                    int res = server.createLandmark(sendString);
                }
            }).start();
        } else {
            Toast.makeText(this, "Failed to create Landmark", Toast.LENGTH_SHORT).show();
        }
    }

    public double getUserLongitude(){
        return user_longitude;
    }

    public double getUserLatitude() {
        return user_latitude;
    }

    //clears map and repins landmarks
    public void refreshMap() {
        Log.i("array", landmarkArrayList.toString());
        if (mMap != null) {
            mMap.clear();
            onMapReady(mMap);
        }
    }

    public void clearLandMarks() {
        landmarkArrayList.clear();
    }
}
