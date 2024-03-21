package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailEditText = findViewById(R.id.editText1);
        passwordEditText = findViewById(R.id.editText2);
        Button loginButton = findViewById(R.id.button2); // Update this ID to match your button's ID in your layout
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        // Find the "Forgot Password?" text view and set an OnClickListener on it
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to start TroubleSigningInActivity
                Intent intent = new Intent(LoginActivity.this, TroubleSigningInActivity.class);
                startActivity(intent);
            }
        });

        // Assuming you have EditText fields for email and password in your layout

    }



    public void attemptLogin() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if ("xxxx".equals(email) && "boran".equals(password)) {
            // Credentials match, move to MainPage
            Intent intent = new Intent(this, MainPage.class);
            startActivity(intent);
            finish(); // Call finish() if you want to remove this activity from the back stack
        } else {
            // Credentials do not match, show error or do nothing
            // You might want to show an AlertDialog or Toast here to inform the user
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_LONG).show();
        }
    }
}
