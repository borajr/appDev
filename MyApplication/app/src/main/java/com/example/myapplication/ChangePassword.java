package com.example.myapplication;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ChangePassword extends AppCompatActivity {

    private EditText editTextNewPassword;
    private EditText editTextRepeatPassword;
    private Button changeButton;
    private OrientationEventListener orientationEventListener;


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

        orientationEventListener = new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int orientation) {
                if (orientation >= 45 && orientation < 135) {
                    // Landscape mode, set screen orientation to reverse portrait
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else if (orientation >= 135 && orientation < 225) {
                    // Upside down mode, set screen orientation to portrait
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
                } else if (orientation >= 225 && orientation < 315) {
                    // Reverse landscape mode, set screen orientation to portrait
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
                } else {
                    // Portrait mode, set screen orientation to portrait
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
            }
        };

        // Start the OrientationEventListener
        orientationEventListener.enable();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Disable the OrientationEventListener to prevent memory leaks
        orientationEventListener.disable();
    }
}
