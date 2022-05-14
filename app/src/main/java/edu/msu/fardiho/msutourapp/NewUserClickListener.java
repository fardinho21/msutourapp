package edu.msu.fardiho.msutourapp;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


public class NewUserClickListener implements View.OnClickListener{

    private MainActivity mainActivity;

    public NewUserClickListener(MainActivity ma) {
        mainActivity = ma;
    }

    @Override
    public void onClick(View view) {
        //TODO: Use Server to send CREATE_USER request #Done
        String pw = ((EditText) mainActivity.findViewById(R.id.password_et))
                .getText().toString();
        String repw = ((EditText) mainActivity.findViewById(R.id.reenterpass_et))
                .getText().toString();
        String username = ((EditText) mainActivity.findViewById(R.id.username_et))
                .getText().toString();

        if (!pw.equals(repw)) {
            mainActivity.notifyUser("Passwords must match!");
            return;
        }

        try {
            Server server = new Server();
            server.RequestToServer(username, pw, "CREATE_USER");
            while ( true ) {
                String res = server.getServerResponse();
                if (res != null && !res.equals("")) {
                    JSONObject obj = new JSONObject(res);
                    if (obj.getString("op").equals("USER_CREATED")) {
                        mainActivity.notifyUser("User Created Success");
                        break;
                    }
                } else {
                    mainActivity.notifyUser("Error Creating User");
                }
                Thread.currentThread().sleep(500);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
