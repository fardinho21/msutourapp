package edu.msu.fardiho.msutourapp.Server;

import android.util.Log;

import com.google.android.gms.common.util.HttpUtils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class Server {


    //base url and login scripts go here
    public static final String BASE_URL = "";
    public static final String CREATE_USER = "";
    public static final String LOGIN = "";
    public static final String LOAD = "";
    public static final String LOAD_LANDMARKS = "";
    public static final String CREATE_LANDMARK = "";
    public static final String MAGIC = "NechAtHa6RuzeR8x";


    //return types can change as we see fit

    public InputStream UserLogin(String username, String password) {
        String query = LOGIN + "?user=" + username + "&magic=" + MAGIC + "&pw=" + password;
        try {
            URL url = new URL(query);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int responseCode = conn.getResponseCode();
            if(responseCode != HttpURLConnection.HTTP_OK) {
                return null;
            }
            InputStream stream = conn.getInputStream();
            return stream;
        } catch (MalformedURLException e) {
            // Should never happen
            return null;
        } catch (IOException ex) {
            return null;
        }
    }

    public InputStream CreateUser(String username, String password) {
        String query = CREATE_USER + "?user=" + username + "&magic=" + MAGIC + "&pw=" + password;
        try {
            URL url = new URL(query);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int responseCode = conn.getResponseCode();
            if(responseCode != HttpURLConnection.HTTP_OK) {
                return null;
            }
            InputStream stream = conn.getInputStream();
            return stream;
        } catch (MalformedURLException e) {
            // Should never happen
            return null;
        } catch (IOException ex) {
            return null;
        }
    }

    //pulls all landmark locations
    public InputStream loadLandmarks() {
        //String query = LOAD_LANDMARKS;
        String query = LOAD + "?magic=" + MAGIC;
        try {
            URL url = new URL(query);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int responseCode = conn.getResponseCode();
            if(responseCode != HttpURLConnection.HTTP_OK) {
                return null;
            }
            InputStream stream = conn.getInputStream();
            return stream;
        } catch (MalformedURLException e) {
            // Should never happen
            return null;
        } catch (IOException ex) {
            return null;
        }
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
    public static void skipToEndTag(XmlPullParser xml) throws IOException, XmlPullParserException {
        int tag;
        do {
            tag = xml.next();
            if(tag == XmlPullParser.START_TAG) {
                // Recurse over any start tag
                skipToEndTag(xml);
            }
        } while(tag != XmlPullParser.END_TAG && tag != XmlPullParser.END_DOCUMENT);
    }
}
