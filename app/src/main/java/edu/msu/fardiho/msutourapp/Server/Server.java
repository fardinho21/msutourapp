package edu.msu.fardiho.msutourapp.Server;

import static java.lang.Thread.currentThread;

import android.util.Log;

import org.json.JSONException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.json.JSONObject;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.lang.Thread;

//Socket implementation
import java.net.Inet4Address;
import java.net.Socket;
import java.net.URL;
import edu.msu.fardiho.msutourapp.Server.ServerRequestThread;

public class Server {

    //base url and login scripts go here
    //public static final String BASE_URL = "10.0.2.2:8080/";
    public static final String SERVER_IP = "10.0.2.2";
    public static final int SERVER_PORT = 8080;
    public static final String CREATE_USER = "";
    public static final String LOGIN = "login";
    public static final String LOAD = "";
    public static final String LOAD_LANDMARKS = "";
    public static final String CREATE_LANDMARK = "";
    public static final String MAGIC = "NechAtHa6RuzeR8x";
    public String serverResponse = null;

    //return types can change as we see fit

    private JSONObject generateJSON(String username, String password, String operation) throws JSONException {
        JSONObject data = new JSONObject();
        data.put("op",operation);
        data.put("username", username);
        data.put("passord", password);
        return data;
    }

    public void setServerResponse(String res) {
        this.serverResponse = res;
    }

    public String getServerResponse() {return serverResponse;}

    public JSONObject getServerResponseObject() throws JSONException {
        return new JSONObject(serverResponse);
    }

    public InputStream RequestToServer(String username, String password, String operation) throws JSONException {
        JSONObject data = generateJSON(username, password, operation);
        ServerRequestThread SerReqTH = new ServerRequestThread(SERVER_PORT, SERVER_IP, data, this);
        //ServerResponseThread SerResTH = new ServerResponseThread();
        //SerReqTH.addListener(SerResTH);
        Thread TH = new Thread(SerReqTH);
        TH.start();
        return null;
    }

    //pulls all landmark locations
    public InputStream loadLandmarks() {
        return null;
    }

    //crate landmark
    public int createLandmark(String xml) {
        //String query = CREATE_LANDMARK +"?xml="+xml;
        String query = CREATE_LANDMARK+xml;
        query = query.replaceAll(" ", "%20");
        String strResult = "";
        try {
            URL url = new URL(query);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int responseCode = conn.getResponseCode();
            if(responseCode != HttpURLConnection.HTTP_OK) {
                return -1;
            }
            InputStream stream = conn.getInputStream();
            InputStreamReader rd = new InputStreamReader(stream,"utf-8");
            BufferedReader in = new BufferedReader(rd);
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = in.readLine()) != null){
                buffer.append(line);
            }
            strResult = buffer.toString();
            stream.close();
            if (strResult.equals("landmark successfully added")){
                return 1;
            }
            else{
                return -1;
            }
        } catch (MalformedURLException e) {
            // Should never happen
            return -1;
        } catch (IOException ex) {
            return -1;
        }
    }
    //TODO : Replace with JSON parser
}
