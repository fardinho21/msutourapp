package edu.msu.fardiho.msutourapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

import edu.msu.fardiho.msutourapp.Server.Server;

public class NewUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
    }

    public void GoTime(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void NoTime(){
        Toast.makeText(this, "User Already Exists", Toast.LENGTH_LONG).show();
    }

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
            InputStream stream = null;
            try {
                stream = server.RequestToServer(username, password, "CREATEUSER");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //TODO: Change XML parser to JSON parser
            boolean fail = stream == null;
            if(!fail) {
                try {
                    XmlPullParser xml = Xml.newPullParser();
                    xml.setInput(stream, "UTF-8");
                    xml.nextTag();      // Advance to first tag
                    xml.require(XmlPullParser.START_TAG, null, "msutour");
                    String status = xml.getAttributeValue(null, "status");
                    if(status.equals("yes")) {
                        GoTime();
                    } else if (status.equals("no")){
                        String error = xml.getAttributeValue(null, "error");
                        if (error.equals("username already exists")){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    NoTime();
                                }
                            });
                        }
                        fail = true;
                    }
                    else{
                        fail = true;
                    }
                } catch(IOException ex) {
                    fail = true;
                } catch(XmlPullParserException ex) {
                    fail = true;
                } finally {
                    try {
                        stream.close();
                    } catch (IOException ex) {

                    }
                }
            }
        }
    }
}