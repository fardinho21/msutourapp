package edu.msu.fardiho.msutourapp;

import android.util.Log;

import org.json.JSONException;
import java.io.IOException;
import java.io.InputStream;

import edu.msu.fardiho.msutourapp.Server.Server;

public class Login {

    private String username = "defaultUser";
    private String password = "deafultPass";
    private MainActivity mainActivity = null;


    Login(String username, String password, MainActivity mainActivity) {
        this.username = username;
        this.password = password;
        this.mainActivity = mainActivity;
    }
    public void onCreateDialog() {

        Server server = new Server();
        InputStream stream = null;
        try {
            server.RequestToServer(username, password, "LOGIN");
            while (true) {
                String res = server.getServerResponse();
                if (server.getServerResponse() != null) {
                    //Log.i("serverResponse",res);

                    if (true) {
                        mainActivity.loginSuccessful = true;
                    }

                    break;
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}