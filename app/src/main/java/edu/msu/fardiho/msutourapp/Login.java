package edu.msu.fardiho.msutourapp;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class Login {

    private String username = "defaultUser";
    private String password = "deafultPass";
    private MainActivity mainActivity = null;

    Login () {

    }

    Login(String username, String password, MainActivity mainActivity) {
        this.username = username;
        this.password = password;
        this.mainActivity = mainActivity;
    }

    public void LoginSuccessful(String username, String userId) {
        //TODO: set the username and userId on the mainActivity
        mainActivity.setLoginStatus(true, username, userId);
    }

    public void onCreateDialog() {

        Server server = new Server();
        String res = "";
        try {
            server.RequestToServer(username, password, "LOGIN");
            while (true) {
                res = server.getServerResponse();
                if (res != null && !res.equals("")) {
                    JSONObject obj = new JSONObject(res); //JSONObject from string
                    if (obj.getString("op").equals("LOGIN_TRUE")) {
                        LoginSuccessful(server.getUsername(),server.getUserId());
                        //TODO: get username and userId from server on login-success
                        break;
                    }
                } else {
                    mainActivity.notifyUser("Invalid Login");
                }
                Thread.currentThread().sleep(500);
            }
        } catch (JSONException | InterruptedException e) {
            e.printStackTrace();
            Log.e("Login", Objects.requireNonNull(e.getMessage()));
        }
    }

}