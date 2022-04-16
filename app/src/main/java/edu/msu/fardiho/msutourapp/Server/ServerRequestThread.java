package edu.msu.fardiho.msutourapp.Server;

import android.util.Log;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.io.PrintWriter;
import java.io.BufferedReader;
import org.json.JSONObject;

public class ServerRequestThread implements Runnable{

    public static int portNumber;
    public static String hostName = "";
    public static JSONObject data = null;
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
        if (data != null) {
            this.data = data;
        }
    }

    @Override
    public final void run() {
        Socket s;
        //ServerSocket serverSocket;
        try {
            s = new Socket(hostName, portNumber);

            output = new PrintWriter(
                    new OutputStreamWriter(s.getOutputStream(),
                            StandardCharsets.UTF_8),
                    true);

            input = new BufferedReader(new InputStreamReader(s.getInputStream()));
            output.println(data.toString()); // SEND DATA TO SERVER

            while (true) {
                serverRef.setServerResponse(input.readLine());
                Log.i("ServerThread.msg", serverRef.getServerResponse());
            }

        }
        catch (Exception e) {
            Log.e("ServerThread: ", Objects.requireNonNull(e.getMessage()));
        }
        finally {
            Log.i("ServerRequestThread: ","thread done");
        }
    }

}