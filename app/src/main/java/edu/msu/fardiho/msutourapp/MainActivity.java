package edu.msu.fardiho.msutourapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //Test variables start
    String username = "user";
    String password = "pass";
    EditText username_text;
    EditText password_text;
    private boolean loginSuccessful = false;

    //test variables end
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username_text = (findViewById(R.id.username_et));
        password_text = (findViewById(R.id.password_et));
        //password_text.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }
    //check password against database record and login
    //if not credentials are incorrect, toast
    public void onLogin(View view) {
        // Get values from form fields
        username = username_text.getText().toString();
        password = password_text.getText().toString();
        Login login = new Login(username, password, this);
        login.onCreateDialog();

        if (loginSuccessful) {
            //writePreferences();
            //instantiate tour intent and bundle
            startTourActivity();
        }
        else {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT)
                    .show();
        }

    }

    public void onCreateNewUserMain(View view) {
        Intent intent = new Intent(this, NewUserActivity.class);

        startActivity(intent);
        //readPreferences();
    }

    public void startTourActivity() {
        Intent tour_intent = new Intent(this,TourActivity.class);
        Bundle b = new Bundle();
        //store user data and bundle
        b.putString("USERNAME",username);
        b.putString("PASS",password);
        tour_intent.putExtras(b);
        //start the tour activity
        startActivity(tour_intent);
    }

    public void setLoginStatus(boolean value) { loginSuccessful = value; }

    public boolean getLoginStatus() { return loginSuccessful; }

}
