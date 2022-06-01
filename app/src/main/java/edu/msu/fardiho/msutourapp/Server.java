package edu.msu.fardiho.msutourapp;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.Thread;

public class Server {

    public static final String SERVER_IP = "10.0.2.2";
    public static final int SERVER_PORT = 8080;
    public String serverResponse = null;
    private String usernameResponse = "";
    private String userIdResponse = "";

    public Server() {
        Log.i("Server()", "server started at " +
                SERVER_IP + " " +
                String.valueOf(SERVER_PORT));
    }

    private JSONObject generateJSONfromUserInput(String username, String password, String operation)
            throws JSONException
    {
        JSONObject data = new JSONObject();
        data.put("op",operation);
        data.put("username", username);
        data.put("password", password);
        return data;
    }

    private JSONObject generateJSONfromUserInput(String username, String userId, String operation, String lm)
            throws JSONException
    {
        JSONObject data = new JSONObject();
        data.put("op",operation);
        data.put("username", username);
        data.put("userId", userId);
        if (operation == "DELETE_LANDMARK")
            data.put("landmarkId", lm);
        else
            data.put("landmark", lm);
        return data;
    }

    //operation: LOGIN, CREATE_USER, FETCH_LANDMARKS
    public void RequestToServer(String username, String password, String operation)
            throws JSONException
    {
        JSONObject data = generateJSONfromUserInput(username, password, operation);
        ServerRequestThread SerReqTH =
                new ServerRequestThread(SERVER_PORT, SERVER_IP, data, this);
        Thread TH = new Thread(SerReqTH);
        TH.start();
    }

    //operation: CREATE_LANDMARK, DELETE_LANDMARK
    public void RequestToServer(String username, String userId, String operation, String landmark)
            throws JSONException
    {
        JSONObject data = generateJSONfromUserInput(username, userId, operation, landmark);
        ServerRequestThread SerReqTH =
                new ServerRequestThread(SERVER_PORT, SERVER_IP, data, this);
        Thread TH = new Thread(SerReqTH);
        TH.start();
    }

    //Setters
    public void setUsername (String res)
            throws JSONException
    {
        JSONObject obj = new JSONObject(res);
        usernameResponse = obj.getString("username");
    }
    public void setUserId (String res)
            throws JSONException
    {
        JSONObject obj = new JSONObject(res);
        userIdResponse = obj.getString("userId");
    }
    public void setServerResponse(String res) {
        this.serverResponse = res;
    }

    //Getters
    public String getServerResponse() {return serverResponse;}
    public JSONObject getServerResponseObject()
            throws JSONException
    {
        return new JSONObject(serverResponse);
    }
    public String getUsername () {return usernameResponse;}
    public String getUserId () {return userIdResponse;}
}
