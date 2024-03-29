package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        EditText editTextEmail = findViewById(R.id.editText1);
        EditText passwordEditText = findViewById(R.id.editText2); // Assuming this is the ID for the password field
        EditText repeatPasswordEditText = findViewById(R.id.editText3); // Assuming this is the ID for the repeat password field
        Button signUpButton = findViewById(R.id.signupButton);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailInput = editTextEmail.getText().toString().trim();
                String password = passwordEditText.getText().toString();
                String repeatPassword = repeatPasswordEditText.getText().toString();

                // Check if the email contains "tue.nl"
                if (emailInput.contains("tue.nl")) {
                    // Intent to navigate to the next Activity (replace NextActivity.class with ConfirmationActivity.class)
                    Intent intent = new Intent(SignupActivity.this, ConfirmationActivity.class); // Corrected to ConfirmationActivity
                    startActivity(intent);
                    mAuth.createUserWithEmailAndPassword(emailInput, emailInput);
                } else {
                    // Show a message if the email does not contain "tue.nl"
                    Toast.makeText(SignupActivity.this, "Please enter a TU/e email address.", Toast.LENGTH_LONG).show();
                    return;
                }

                if(emailInput.isEmpty()) { // Extend this condition for all fields
                    Toast.makeText(SignupActivity.this, "Please fill in all fields.", Toast.LENGTH_LONG).show();
                    return;
                }

                // Then, check if passwords match and are strong enough
                if (!password.equals(repeatPassword)) {
                    Toast.makeText(SignupActivity.this, "Passwords do not match.", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!isValidPassword(password)) {
                    Toast.makeText(SignupActivity.this, "Password must contain 8+ characters, a letter, a number, and a capital letter.", Toast.LENGTH_LONG).show();
                    return;
                }

                // If all checks pass, proceed to the ConfirmationActivity
                Intent intent = new Intent(SignupActivity.this, ConfirmationActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 8) {
            return false; // Password too short
        }
        boolean hasLetter = false;
        boolean hasDigit = false;
        boolean hasCapitalLetter = false;

        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {
                hasLetter = true;
            }
            if (Character.isDigit(c)) {
                hasDigit = true;
            }
            if (Character.isUpperCase(c)) {
                hasCapitalLetter = true;
            }
            if (hasLetter && hasDigit && hasCapitalLetter) {
                return true; // Password meets all criteria
            }
        }
        return false; // Password missing required character types
    }
}
