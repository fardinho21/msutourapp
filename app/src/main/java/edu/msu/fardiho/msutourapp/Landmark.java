package edu.msu.fardiho.msutourapp;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

public class Landmark {

    private MarkerOptions mark;
    private String description;
    private String name;
    private String dataBaseID;
    private float lat;
    private float lon;

    Landmark () {
        lat = 0; lon = 0; description = ""; name = "";
    }
    Landmark (String des, float la, float lo, String n) {
        lat = la; lon = lo; description = des; name = n;
        mark = new MarkerOptions()
                .position(new LatLng(lat, lon))
                .title(n);
    }

    //Getters
    public MarkerOptions getMarkOps() {return mark;}
    public float getLat(){return lat;}
    public float getLon(){return lon;}
    public String getDesc(){return description;}
    public String getName() {
        return name;
    }
    public String getLandMarkString() {
        return lat + " " + lon + " : " + description + " " + name;
    }
    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("lat", String.valueOf(lat));
            obj.put("lon", String.valueOf(lon));
            obj.put("desc", description);
            obj.put("name", name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    //Setters
    public void setDataBaseID(String id) {dataBaseID = id;}
    public void setMarkOps(MarkerOptions m) {
        mark = m;
    }
    public void setDesc(String desc){
        description = desc;
    }

}
