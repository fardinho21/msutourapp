package edu.msu.fardiho.msutourapp;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Objects;

//TODO: Login freezes when entering a non-existent user. Needs Fix.
public class Login {

    private String username = "defaultUser";
    private String password = "defaultPass";
    private MainActivity mainActivity = null;

    Login(String username, String password, MainActivity mainActivity) {
        this.username = username;
        this.password = password;
        this.mainActivity = mainActivity;
    }

    public void LoginSuccessful(String username, String userId) {
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
                        break;
                    }
                } else {
                    server.NotifyUser("Invalid Login",mainActivity);
                }
                Thread.currentThread().sleep(500);
            }
        } catch (JSONException | InterruptedException e) {
            e.printStackTrace();
            Log.e("Login", Objects.requireNonNull(e.getMessage()));
        }
    }

}