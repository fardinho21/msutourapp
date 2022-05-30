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

    public MarkerClickListener(TourActivity ta) {
        tourActivity = ta;
        landmarkArrayList = ta.getLandmarkArrayList();
    }


    LndMrkDescDlg lndMrkDescDlg;
    @Override
    public boolean onMarkerClick(Marker marker) {

        if(marker.getId().equals("m0")){
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
        lndMrkDescDlg.show( tourActivity.getSupportFragmentManager(), "desc");
        return false;
    }
}
