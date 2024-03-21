package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileCloneTwo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiletwoclone);

        // Find the button by its ID
        View btnContinue = findViewById(R.id.confirm_button);


        // Set OnClickListener to the button
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the activity_change_password.xml
                Intent intent = new Intent(ProfileCloneTwo.this, ProfileSetup.class);
                startActivity(intent); // Start the new activity
            }
        });
    }
}
