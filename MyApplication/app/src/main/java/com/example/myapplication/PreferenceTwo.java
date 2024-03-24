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
        setContentView(R.layout.activity_profiledetailtwo); // Ensure this matches your layout file

        // Initialize your views
        genderSpinner = findViewById(R.id.editText1);
        minHeightSpinner = findViewById(R.id.minHeightSpinner);
        maxHeightSpinner = findViewById(R.id.maxHeightSpinner);
        minAgeSpinner = findViewById(R.id.minAgeSpinner);
        maxAgeSpinner = findViewById(R.id.maxAgeSpinner);
        confirmButton = findViewById(R.id.confirm_button);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validatePreferences()) {
                    // Move to the next appropriate page
                    Intent nextIntent = new Intent(PreferenceTwo.this, ProfileSetup.class); // Adjust as needed
                    startActivity(nextIntent);
                }
            }
        });
    }

    private boolean validatePreferences() {
        int minHeightIndex = minHeightSpinner.getSelectedItemPosition();
        int maxHeightIndex = maxHeightSpinner.getSelectedItemPosition();
        if (minHeightIndex > maxHeightIndex) {
            Toast.makeText(this, "Minimum height cannot be greater than maximum height", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate ages
        int minAgeIndex = minAgeSpinner.getSelectedItemPosition();
        int maxAgeIndex = maxAgeSpinner.getSelectedItemPosition();
        if (minAgeIndex > maxAgeIndex) {
            Toast.makeText(this, "Minimum age cannot be greater than maximum age", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true; // All validations passed
    }
}
