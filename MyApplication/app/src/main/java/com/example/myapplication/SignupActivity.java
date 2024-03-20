package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Find the EditText and Button by their ID
        EditText editTextEmail = findViewById(R.id.editText1);
        Button signUpButton = findViewById(R.id.button2);

        // Set a click listener on the button
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailInput = editTextEmail.getText().toString().trim();

                // Check if the email contains "tue.nl"
                if (emailInput.contains("tue.nl")) {
                    // Intent to navigate to the next Activity (replace NextActivity.class with ConfirmationActivity.class)
                    Intent intent = new Intent(SignupActivity.this, ConfirmationActivity.class); // Corrected to ConfirmationActivity
                    startActivity(intent);
                } else {
                    // Show a message if the email does not contain "tue.nl"
                    Toast.makeText(SignupActivity.this, "Please enter a TU/e email address.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
