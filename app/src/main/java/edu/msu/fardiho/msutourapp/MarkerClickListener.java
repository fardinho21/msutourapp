package edu.msu.fardiho.msutourapp;

import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

public class MarkerClickListener
        implements GoogleMap.OnMarkerClickListener {

    private TourActivity tourActivity;
    private ArrayList<Landmark> landmarkArrayList = new ArrayList<Landmark>();
    LndMrkDescDlg lndMrkDescDlg;

    //TODO: Display image with description onMarkerClick

    public MarkerClickListener() {}
    public MarkerClickListener(TourActivity ta) {
        setTourActivity(ta);
        setLandmarkArrayList(ta.getLandmarkArrayList());
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        if(marker.getId().equals("m0")){
            return false;
        }

        //get landmark position
        LatLng latLng = marker.getPosition();
        Log.i("latlng", latLng.toString());
        String name = "Name: ";
        String desc = "Description: ";
        //find the landmark in the array
        Landmark found = findLandmarkFromMarker(marker);

        if (tourActivity.getDeleteModeState()) {
            tourActivity.deleteLandmark(found);
        } else {
            name += found.getName();
            desc += found.getDesc();
            showLandmarkInfoDialog(name, desc);
        }
        //TEST COMMENT
        return false;
    }

    //Setters
    public void setTourActivity(TourActivity ta) {tourActivity = ta;}
    public void setLandmarkArrayList(ArrayList<Landmark> lmarray) {landmarkArrayList = lmarray;}

    //utility
    private Landmark findLandmarkFromMarker(Marker marker) {
        for (Landmark l : landmarkArrayList){
            if ( marker.getTitle().equals(l.getName()))
                return l;
        }
        return null;
    }
    private void showLandmarkInfoDialog(String name, String desc) {
        lndMrkDescDlg = new LndMrkDescDlg(name, desc);
        lndMrkDescDlg.show( tourActivity.getSupportFragmentManager(), "desc");
    }
}
