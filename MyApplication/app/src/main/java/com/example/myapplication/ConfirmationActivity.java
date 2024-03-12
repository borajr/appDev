package com.example.myapplication; // Replace with your actual package name

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation); // Make sure this is your confirmation layout

        final EditText editTextCode = findViewById(R.id.editText1); // Your code input EditText
        Button confirmButton = findViewById(R.id.button2); // Your confirmation Button

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredCode = editTextCode.getText().toString().trim();

                // Check if the entered text is "3169"
                if ("3169".equals(enteredCode)) {
                    // Correct code, transition to ProfileCreationDetailOne
                    Intent intent = new Intent(ConfirmationActivity.this, ProfileCreationDetailOne.class);
                    startActivity(intent);
                } else {
                    // Incorrect code, show a toast message
                    Toast.makeText(ConfirmationActivity.this, "Incorrect code. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
