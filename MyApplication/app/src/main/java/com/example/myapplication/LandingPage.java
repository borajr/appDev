package com.example.myapplication;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * LandingPage checks if the user is already signed in and redirects to the appropriate page.
 */
public class LandingPage extends AppCompatActivity {
    FirebaseAuth mAuth;
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            // If no user is currently signed in, navigate to the login activity.
            Intent intent = new Intent(LandingPage.this, MainActivity.class);

        } else {
            // If a user is found to be signed in, navigate to the main page of the app.
            Intent intent = new Intent(LandingPage.this, MainPage.class);
        }
    }
}