package edu.msu.fardiho.msutourapp.Server;

import android.util.Log;
import org.json.JSONException;
import java.io.InputStream;
import org.json.JSONObject;
import java.lang.Thread;

public class Server {

    public static final String SERVER_IP = "10.0.2.2";
    public static final int SERVER_PORT = 8080;
    public String serverResponse = null;

    private JSONObject generateJSON(String username, String password, String operation) throws JSONException {
        JSONObject data = new JSONObject();
        data.put("op",operation);
        data.put("username", username);
        data.put("password", password);
        return data;
    }

    public void setServerResponse(String res) {
        this.serverResponse = res;
    }

    public String getServerResponse() {return serverResponse;}

    public JSONObject getServerResponseObject() throws JSONException {
        return new JSONObject(serverResponse);
    }
    //operation: LOGIN, CREATE_USER, CREATE_LANDMARK
    public InputStream RequestToServer(String username, String password, String operation) throws JSONException {
        JSONObject data = generateJSON(username, password, operation);
        ServerRequestThread SerReqTH = new ServerRequestThread(SERVER_PORT, SERVER_IP, data, this);
        Thread TH = new Thread(SerReqTH);
        TH.start();
        return null;
    }

}
