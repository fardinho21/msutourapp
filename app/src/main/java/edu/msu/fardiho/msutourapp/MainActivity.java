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
    public boolean loginSuccessful = false;

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
        String un = username_text.getText().toString();
        String pw = password_text.getText().toString();
        Login login = new Login(un, pw, this);
        login.onCreateDialog();
        try{
            Thread.currentThread().sleep(1000);
        }
        catch (InterruptedException e) { }
        if (loginSuccessful) {
            //writePreferences();
            //instantiate tour intent and bundle
            Intent tour_intent = new Intent(this,TourActivity.class);
            Bundle b = new Bundle();
            //store user data and bundle
            b.putString("USERNAME",un);
            b.putString("PASS",pw);
            tour_intent.putExtras(b);
            //start the tour activity
            startActivity(tour_intent);
        }
        else {
            Toast.makeText(this,"Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }

    public void onCreateNewUserMain(View view) {
        Intent intent = new Intent(this, NewUserActivity.class);
        startActivity(intent);
        //readPreferences();
    }

    public void setLoginSuccessful(boolean success) { loginSuccessful = success;}
}
