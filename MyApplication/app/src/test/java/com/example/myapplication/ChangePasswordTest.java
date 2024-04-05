package com.example.myapplication;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowToast;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {28})
public class ChangePasswordTest {

    private ChangePassword activity;

    @Mock
    private EditText mockEditTextNewPassword;
    @Mock
    private EditText mockEditTextRepeatPassword;
    @Mock
    private Button mockChangeButton;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        // Initialize the activity
        activity = Robolectric.buildActivity(ChangePassword.class)
                .create()
                .resume()
                .get();
    }

    @Test
    public void passwordsDoNotMatch_showsToast() {
        EditText editTextNewPassword = activity.findViewById(R.id.editText3);
        EditText editTextRepeatPassword = activity.findViewById(R.id.editText4);
        Button changeButton = activity.findViewById(R.id.signupButton);

        // Simulate user input
        editTextNewPassword.setText("Password123");
        editTextRepeatPassword.setText("DifferentPassword123");

        // Simulate button click
        changeButton.performClick();

        // Verify Toast message
        assertEquals("Passwords do not match.", ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void passwordNotStrongEnough_showsToast() {
        EditText editTextNewPassword = activity.findViewById(R.id.editText3);
        EditText editTextRepeatPassword = activity.findViewById(R.id.editText4);
        Button changeButton = activity.findViewById(R.id.signupButton);

        // Simulate user input
        editTextNewPassword.setText("pass");  // Not strong enough
        editTextRepeatPassword.setText("pass");

        // Simulate button click
        changeButton.performClick();

        // Verify Toast message
        assertEquals("Password must contain 8+ characters, a letter, a number, and a capital letter.", ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void passwordStrongAndMatches_noToastDisplayed() {
        EditText editTextNewPassword = activity.findViewById(R.id.editText3);
        EditText editTextRepeatPassword = activity.findViewById(R.id.editText4);
        Button changeButton = activity.findViewById(R.id.signupButton);

        // Assuming you handle the successful password change with a toast or some action,
        // and that action is what you're verifying. Here, we're just checking that no toast is displayed
        // for simplicity, assuming that's your handling mechanism.

        // Simulate user input for a valid case
        editTextNewPassword.setText("Password1A");
        editTextRepeatPassword.setText("Password1A");

        // Simulate button click
        changeButton.performClick();

        // Assert no toast was shown (assuming showing a toast on success)
        // This might need to change based on your success handling mechanism
        assertNull(ShadowToast.getLatestToast());
    }

    // Additional tests could include verifying the successful password change flow,
    // such as interactions with a mocked backend service.
}
