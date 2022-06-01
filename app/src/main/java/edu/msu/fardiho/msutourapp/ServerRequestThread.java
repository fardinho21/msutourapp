package edu.msu.fardiho.msutourapp;

import android.annotation.SuppressLint;
import android.util.Log;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.io.PrintWriter;
import java.io.BufferedReader;

import org.json.JSONException;
import org.json.JSONObject;

public class ServerRequestThread implements Runnable{

    public static int portNumber;
    public static String hostName = "";
    public static Object  data = null;
    private PrintWriter output;
    private BufferedReader input;
    private Server serverRef = null;

    //constructor
    private void connectionInfo(int portNumber, String hostName, Server serverRef) {
        this.portNumber = portNumber;
        this.hostName = hostName;
        this.serverRef = serverRef;
    }

    ServerRequestThread(int portNumber, String hostName, JSONObject data, Server serverRef) {
        this.connectionInfo(portNumber, hostName, serverRef);
        if (data != null)
            this.data = data;
    }

    @SuppressLint("LongLogTag")
    @Override
    public final void run() {
        Socket s;
        String response = "";
        try {
            s = new Socket(hostName, portNumber);

            output = new PrintWriter(
                    new OutputStreamWriter(s.getOutputStream(),
                            StandardCharsets.UTF_8),
                    true);

            input = new BufferedReader(new InputStreamReader(s.getInputStream()));
            output.println(data.toString()); // SEND DATA TO SERVER

            while (true) {
                response = input.readLine();
                if (response != null && !response.equals("")) {
                    serverRef.setServerResponse(response);
                    Log.i("ServerThread.serverRef.getServerResponse():",
                            serverRef.getServerResponse());
                    break;
                }
            }
        } catch (Exception e) {
            Log.e("ServerThread ", Objects.requireNonNull(e.getMessage()));
        } finally {
            Log.i("ServerRequestThread ","thread done");
            Thread.currentThread().interrupt();
        }
    }

}
