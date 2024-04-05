package com.example.myapplication;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity to allow users to change their password with validations for password matching and strength.
 */
public class ChangePassword extends AppCompatActivity {

    private EditText editTextNewPassword;
    private EditText editTextRepeatPassword;
    private Button changeButton;
    private OrientationEventListener orientationEventListener;

    /**
     * Called when the activity is starting.
     * Initializes the activity, UI components, and sets up the orientation event listener.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle). Otherwise it is null.
     */
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

    /**
     * Checks if the provided password meets the specified strength criteria.
     *
     * @param password The password to check.
     * @return True if the password is strong, false otherwise.
     */
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

    /**
     * Disables the OrientationEventListener when the activity is destroyed to prevent memory leaks.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Disable the OrientationEventListener to prevent memory leaks
        orientationEventListener.disable();
    }
}
