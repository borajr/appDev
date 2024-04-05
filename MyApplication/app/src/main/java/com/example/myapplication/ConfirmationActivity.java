package com.example.myapplication;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * ConfirmationActivity handles the email verification process for users.
 * It provides options to resend verification email and proceed after email verification.
 */
public class ConfirmationActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance(); // Firebase authentication instance
    private FirebaseUser currentUser = mAuth.getCurrentUser(); // Current signed-in user
    private Handler handler;
    private boolean isVerified;
    long startTime; // Start time for email verification timeout



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation); // Sets the layout for the confirmation screen
        long startTime = Timestamp.now().toDate().getTime(); // Initialize start time
        mAuth = FirebaseAuth.getInstance(); // Initialize Firebase Auth
        currentUser = mAuth.getCurrentUser(); // Get current user

        // Initialize buttons for resending code and continuing after verification
        Button resendCode = findViewById(R.id.resendCode); // Your confirmation Button
        Button continueb = findViewById(R.id.button2);

        // Set onClick listener for continue button
        continueb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUser.reload();
                long timePassedMillis = Timestamp.now().toDate().getTime() - startTime;
                long timePassedMinutes = timePassedMillis / (60 * 1000); // Convert milliseconds to minutes
                if (currentUser.isEmailVerified() && timePassedMinutes < 30) {
                    Toast.makeText(ConfirmationActivity.this, "Successfully verified", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ConfirmationActivity.this, ProfileCreationDetailOne.class);
                    startActivity(intent);
                    finish();
                } else if (currentUser.isEmailVerified() && timePassedMinutes > 30) {
                    Toast.makeText(ConfirmationActivity.this, "Time expired", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ConfirmationActivity.this, "Email not verified", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Set onClick listener for resend code button
        resendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmailVerification(); // Call method to resend verification email
            }

        });


    }

    /**
     * Sends an email verification to the user's email.
     */
    private void sendEmailVerification() {
        currentUser.reload();
        // Attempt to send the email verification
        currentUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("ConfirmationActivity", "Email sent."); // Log success
                    startTime = Timestamp.now().toDate().getTime(); // Reset start time for verification
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("ERROR", e.toString()); // Log failure

            }
        });
    }



}