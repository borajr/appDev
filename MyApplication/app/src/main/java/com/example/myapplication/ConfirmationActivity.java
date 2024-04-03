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

public class ConfirmationActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private Handler handler;
    private boolean isVerified;
    long startTime;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation); // Make sure this is your confirmation layout
        long startTime = Timestamp.now().toDate().getTime();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        Log.d("CURRUSER", currentUser.toString());

        Button resendCode = findViewById(R.id.resendCode); // Your confirmation Button
        Button continueb = findViewById(R.id.button2);

        continueb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUser.reload();
                long timePassedMillis = Timestamp.now().toDate().getTime() - startTime;
                long timePassedMinutes = timePassedMillis / (60 * 1000); // Convert milliseconds to minutes
                if (currentUser.isEmailVerified() && timePassedMinutes < 3) {
                    Toast.makeText(ConfirmationActivity.this, "Successfully verified", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ConfirmationActivity.this, ProfileCreationDetailOne.class);
                    startActivity(intent);
                    finish();
                } else if (currentUser.isEmailVerified() && timePassedMinutes > 3) {
                    Toast.makeText(ConfirmationActivity.this, "Time expired", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ConfirmationActivity.this, "Email not verified", Toast.LENGTH_SHORT).show();
                }
            }
        });
        resendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmailVerification();
            }

        });


    }

    private void sendEmailVerification() {
        currentUser.reload();
        currentUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("ConfirmationActivity", "Email sent.");
                    startTime = Timestamp.now().toDate().getTime();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("ERROR", e.toString());

            }
        });
    }



}