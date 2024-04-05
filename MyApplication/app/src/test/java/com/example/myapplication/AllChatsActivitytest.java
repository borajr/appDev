package com.example.myapplication;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

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
public class AllChatsActivitytest {

    private AllChatsActivity allChatsActivity;
    private FirebaseAuth mockAuth;

    @Before
    public void setUp() {
        // Initialize Firebase
        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext());

        // Mock FirebaseAuth and FirebaseUser
        mockAuth = mock(FirebaseAuth.class);
        FirebaseUser mockUser = mock(FirebaseUser.class);

        Mockito.when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        Mockito.when(mockUser.getEmail()).thenReturn("test@example.com");

        // Create Intent to start AllChatsActivity
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), AllChatsActivity.class);
        // Start AllChatsActivity with the intent
        allChatsActivity = Robolectric.buildActivity(AllChatsActivity.class, intent).create().get();
    }

    @Test
    public void allChatsActivity_ShouldNOT_be_Null() {
        // Check that the AllChatsActivity is not null
        assertNotNull("AllChatsActivity is null", allChatsActivity);
    }

    // More detailed tests can be added here for each functionality within the AllChatsActivity.
}
