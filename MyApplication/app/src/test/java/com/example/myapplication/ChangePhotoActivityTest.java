package com.example.myapplication;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {28})
public class ChangePhotoActivityTest {

    private ChangePhotoActivity changePhotoActivity;
    private FirebaseAuth mockAuth;
    private FirebaseUser mockUser;

    @Before
    public void setUp() {
        // Initialize Firebase
        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext());

        // Mock FirebaseAuth and FirebaseUser
        mockAuth = mock(FirebaseAuth.class);
        mockUser = mock(FirebaseUser.class);

        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getEmail()).thenReturn("user@example.com");

        // Start the activity
        changePhotoActivity = Robolectric.buildActivity(ChangePhotoActivity.class, new Intent()).create().start().resume().get();
    }

    @Test
    public void changePhotoActivity_ShouldNOT_be_Null() {
        // Verify the activity is created
        assertNotNull(changePhotoActivity);
    }

    // You can write more tests below to check different functionalities of your activity.
}
