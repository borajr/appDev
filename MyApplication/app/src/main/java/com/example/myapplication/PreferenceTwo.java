package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class PreferenceTwo extends AppCompatActivity {

    private Spinner genderSpinner, minHeightSpinner, maxHeightSpinner, minAgeSpinner, maxAgeSpinner;
    private Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference_detail); // Ensure this matches your layout file

        View btnProfile = findViewById(R.id.confirm_button);

        // Set OnClickListener to the button
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the activity_change_password.xml
                Intent intent = new Intent(PreferenceTwo.this, MainPage.class);
                startActivity(intent); // Start the new activity
            }
        });

    }
}
