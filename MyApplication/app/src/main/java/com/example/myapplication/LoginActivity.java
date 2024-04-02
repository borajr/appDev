package com.example.myapplication;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static final String TAG = "LoginActivity";

    @Override

    public void onStart() {

        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        //if(currentUser != null){

        FirebaseAuth.getInstance().signOut();

    //}
    }

    private EditText emailEditText;
    private EditText passwordEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        EditText editTextEmail = findViewById(R.id.editText1);
        EditText passwordEditText = findViewById(R.id.editText2);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailEditText = findViewById(R.id.editText1);
        passwordEditText = findViewById(R.id.editText2);
        Button loginButton = findViewById(R.id.loginbutton); // Update this ID to match your button's ID in your layout
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

        findViewById(R.id.loginbutton).setOnClickListener(new View.OnClickListener() {
            final EditText emailEditText = findViewById(R.id.editText1);
            final EditText passwordEditText = findViewById(R.id.editText2);
            @Override
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance();
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                signIn(email, password);
            }
        });




    }


    private void signIn(String email, String password) {
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(LoginActivity.this, MainPage.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



    public void attemptLogin() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if ("xxxx".equals(email) && "boran".equals(password)) {
            // Credentials match, move to MainPage
            Intent intent = new Intent(this, MainPage.class);
            startActivity(intent);
            finish(); // Call finish() if you want to remove this activity from the back stack
        } else if ("admin".equals(email) && "YakisikliGuvenlik".equals(password)){
            Intent intent = new Intent(this, AdminActivity.class);
            startActivity(intent);
            finish();

        }else if ("admin".equals(email) && "Bora2003".equals(password)) {
            Intent intent = new Intent(this, AdminActivity.class);
            startActivity(intent);
            finish();
        }else {
            // Credentials do not match, show error or do nothing
            // You might want to show an AlertDialog or Toast here to inform the user
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_LONG).show();
        }
    }




}
