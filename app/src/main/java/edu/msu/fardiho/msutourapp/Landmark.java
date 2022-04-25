package edu.msu.fardiho.msutourapp;

import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

public class Landmark {
    private MarkerOptions mark;
    private String description;
    private String name;
    private float lat;
    private float lon;
    public float getLat(){return lat;}
    public float getLon(){return lon;}
    public String getDesc(){return description;}
    public String getName() {
        return name;
    }
    public void setMarkOps(MarkerOptions m) {
        mark = m;
    }
    public MarkerOptions getMarkOps() {return mark;}
    public void setDesc(String desc){
        description = desc;
    }

    public JSONObject getJSONObject() { return new JSONObject(); }

    public String getJSONString() { return new JSONObject().toString(); }

    Landmark (String des, float la, float lo, String n) {
        lat = la; lon = lo; description = des; name = n;
    }
    Landmark () {
        lat = 0; lon = 0; description = ""; name = "";
    }
}
