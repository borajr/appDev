package com.example.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String TAG = "EmailPassword";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        // Find the EditText and Button by their ID
        EditText editTextEmail = findViewById(R.id.editText1);
        EditText editTextPassword = findViewById(R.id.editText2);
        Button signUpButton = findViewById(R.id.button2);
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Set a click listener on the button
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the email contains "tue.nl"
                if (email.contains("tue.nl")) {
                    mAuth.createUserWithEmailAndPassword(email, password);
                } else {
                    // Show a message if the email does not contain "tue.nl"
                    Toast.makeText(SignupActivity.this, "Please enter a TU/e email address.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}