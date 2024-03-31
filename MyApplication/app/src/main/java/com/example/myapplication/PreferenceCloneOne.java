package com.example.myapplication;

import static com.example.myapplication.R.*;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class PreferenceCloneOne extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_preference_cloneone);


        // Find the button by its ID
        View btnConfirm = findViewById(R.id.confirm_button);


        // Set OnClickListener to the button
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the activity_change_password.xml
                Intent intent = new Intent(PreferenceCloneOne.this, ProfileSetup.class);
                startActivity(intent); // Start the new activity
            }
        });
    }
}
