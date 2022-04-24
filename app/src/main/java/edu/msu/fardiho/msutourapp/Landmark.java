package edu.msu.fardiho.msutourapp;

import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

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

    public JSONObject getJSONObject() { return new JSONObject(); }

    public String getJSONString() { return new JSONObject().toString(); }

    Landmark (String des, float la, float lo, String n) {
        lat = la; lon = lo; desc = des; name = n;
    }
    Landmark () {
        lat = 0; lon = 0; desc = ""; name = "";
    }
}
