package com.example.myapplication; // Replace with your actual package name

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileCreationDetailOne extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content view to your layout
        setContentView(R.layout.activity_profiledetailone); // Ensure this matches your XML file name

        // Initialize your UI components here
        EditText firstNameEditText = findViewById(R.id.editText1);
        EditText lastNameEditText = findViewById(R.id.editText2);
        DatePicker datePicker = findViewById(R.id.datePicker); // If you plan to use the DatePicker value

        Button confirmButton = findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Example action: Show a toast message when the confirm button is clicked
                String firstName = firstNameEditText.getText().toString().trim();
                String lastName = lastNameEditText.getText().toString().trim();

                // Simple validation
                if (firstName.isEmpty() || lastName.isEmpty()) {
                    Toast.makeText(ProfileCreationDetailOne.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Proceed with further actions, e.g., opening a new activity or saving data
                    // For demonstration, just showing a toast message
                    Toast.makeText(ProfileCreationDetailOne.this, "Profile Part One Saved", Toast.LENGTH_SHORT).show();

                    // Example: Navigate to another activity
                    // Intent intent = new Intent(ProfileCreationDetailOne.this, NextActivity.class);
                    // startActivity(intent);
                }
            }
        });

        // Further initialization can be done as needed
    }
}
