package edu.msu.fardiho.msutourapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.Objects;

import edu.msu.fardiho.msutourapp.Server.Server;
import edu.msu.fardiho.msutourapp.Login;

public class NewUserActivity extends AppCompatActivity {

    public MainActivity mainActivity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
    }

    public void setMainActivity(MainActivity ma) { mainActivity = ma; }

    public void onCreateUser(View view) {

        // Get values from form fields
        EditText usernameText = findViewById(R.id.usernameInput);
        EditText passwordText = findViewById(R.id.passwordInput);
        EditText confirmPassTest = findViewById(R.id.confirmPasswordInput);
        final String username = usernameText.getText().toString();
        final String password = passwordText.getText().toString();
        String confirmPassword = confirmPassTest.getText().toString();

        // Ensure passwords match
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
        }

        //create user
        else {
            Server server = new Server();
            String res = "";
            try {
                server.RequestToServer(username, password, "CREATE_USER");
                while (true) {
                    res = server.getServerResponse();
                    if (res != null && !res.equals("")) {
                        JSONObject obj = new JSONObject(res);
                        if (obj.getString("op").equals("USER_CREATED")) {
                            Toast.makeText(this, "User created successfully!", Toast.LENGTH_SHORT)
                                    .show();

                            //ERROR : Android application cannot be cast to MainActivity
                            new Login(username, password, mainActivity)
                                    .LoginSuccessful();
                            ((MainActivity) getApplicationContext())
                                    .startTourActivity();
                            break;
                        }
                    } else {
                        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                    }

                    Thread.currentThread().sleep(500);
                }

            } catch (Exception e) {
                Log.i("NewUserActivity", Objects.requireNonNull(e.getMessage()));
            }
        }
    }
}