package com.example.myapplication;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {28})
public class AdminTest {

    private AdminActivity adminActivity;

    @Before
    public void setUp() {
        // Initialize Firebase
        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext());
        // Mock FirebaseAuth as we are not signing in while testing
        FirebaseAuth mockedAuth = mock(FirebaseAuth.class);
        when(mockedAuth.getCurrentUser()).thenReturn(null);

        // Create Intent to start AdminActivity
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), AdminActivity.class);
        // Start AdminActivity with the intent
        adminActivity = Robolectric.buildActivity(AdminActivity.class, intent).create().get();
    }

    @Test
    public void adminActivity_ShouldNOT_be_Null() {
        // Check that the AdminActivity is not null
        assertNotNull("AdminActivity is null", adminActivity);
    }

    // More detailed tests can be added here for each functionality within the AdminActivity.
}
