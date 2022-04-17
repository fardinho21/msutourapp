package edu.msu.fardiho.msutourapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //Test variables start
    String username = "user";
    String password = "pass";
    EditText username_text;
    EditText password_text;
    EditText reenter_pass_text;
    private boolean createUserMode = false;
    private boolean loginSuccessful = false;

    //test variables end
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        reenter_pass_text = (findViewById(R.id.reenterpass_et));
        username_text = (findViewById(R.id.username_et));
        password_text = (findViewById(R.id.password_et));
        reenter_pass_text.setVisibility(View.GONE);

    }
    //check password against database record and login
    //if not credentials are incorrect, toast

    @Override
    public void onClick(View view) {
        onLogin(view);
    }

    public void onLogin(View view)  {
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

    //toggles the layout to display EditText fields for creating a user
    //also swaps out clicklisteners and text for login button
    public void onCreateNewUserMain(View view) {

        EditText newUser_et = (EditText)findViewById(R.id.reenterpass_et);
        Button logIn_btn = (Button)findViewById(R.id.login);
        Button newUser_btn = (Button)findViewById(R.id.NewUser);
        if (!createUserMode) {
            createUserMode = true;
            logIn_btn.setOnClickListener(new NewUserClickListener(this));
            logIn_btn.setText(R.string.login_btn_text_CreateUser);
            newUser_et.setVisibility(View.VISIBLE);
            newUser_btn.setText(R.string.newuser_btn_text_BackToLogin);
        } else {
            createUserMode = false;
            logIn_btn.setOnClickListener(this);
            logIn_btn.setText(R.string.login_btn_text_Login);
            newUser_et.setVisibility(View.GONE);
            newUser_btn.setText(R.string.newuser_btn_text_NewUser);
        }
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

    public void notifyUser(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


}
