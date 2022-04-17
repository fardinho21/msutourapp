package edu.msu.fardiho.msutourapp;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

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

    public void LoginSuccessful() {
        mainActivity.setLoginStatus(true);
    }

    public void LoginFailed() {
        mainActivity.setLoginStatus(false);
    }

    public void onCreateDialog() {

        Server server = new Server();
        String res = "";
        try {
            server.RequestToServer(username, password, "LOGIN");
            while (true) {
                res = server.getServerResponse();
                if (res != null && !res.equals("")) {
                    JSONObject obj = new JSONObject(res);
                    if (obj.getString("op").equals("LOGIN_TRUE")) {
                        LoginSuccessful();
                        break;
                    }
                } else {
                    Toast.makeText(mainActivity, "Invalid Login", Toast.LENGTH_SHORT).show();
                }
                Thread.currentThread().sleep(500);
            }
        } catch (JSONException | InterruptedException e) {
            e.printStackTrace();
            Log.e("Login", Objects.requireNonNull(e.getMessage()));
        }
    }

}