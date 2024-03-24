package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ChangePassword extends AppCompatActivity {

    private EditText editTextNewPassword;
    private EditText editTextRepeatPassword;
    private Button changeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        editTextNewPassword = findViewById(R.id.editText3);
        editTextRepeatPassword = findViewById(R.id.editText4);
        changeButton = findViewById(R.id.signupButton);

        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPassword = editTextNewPassword.getText().toString();
                String repeatPassword = editTextRepeatPassword.getText().toString();

                if (!newPassword.equals(repeatPassword)) {
                    Toast.makeText(ChangePassword.this, "Passwords do not match.", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!isPasswordStrong(newPassword)) {
                    Toast.makeText(ChangePassword.this, "Password must contain 8+ characters, a letter, a number, and a capital letter.", Toast.LENGTH_LONG).show();
                    return;
                }

                // Proceed with password change logic here
                // For example, update the user's password in your database
            }
        });
    }

    private boolean isPasswordStrong(String password) {
        boolean hasLetter = false;
        boolean hasDigit = false;
        boolean hasUpperCase = false;

        if (password.length() < 8) {
            return false;
        }

        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {
                hasLetter = true;
            }
            if (Character.isDigit(c)) {
                hasDigit = true;
            }
            if (Character.isUpperCase(c)) {
                hasUpperCase = true;
            }

            if (hasLetter && hasDigit && hasUpperCase) {
                return true;
            }
        }

        return false;
    }
}
