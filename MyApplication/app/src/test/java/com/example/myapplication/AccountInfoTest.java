package com.example.myapplication;

import static org.junit.Assert.assertEquals;

import android.content.Intent;
import android.os.Build;
import android.widget.Button;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Build.VERSION_CODES.O_MR1})
public class AccountInfoTest {

    private AccountInfo activity;

    @Before
    public void setUp() {
        // Given
        activity = Robolectric.buildActivity(AccountInfo.class)
                .create()
                .visible()
                .get();
    }

    @Test
    public void whenOnCreate_thenCorrectIntentStarted() {
        // When
        Button btnMain = activity.findViewById(R.id.btnmain);
        btnMain.performClick();

        // Then
        Intent expectedIntent = new Intent(activity, MainPage.class);
        Intent actualIntent = Shadows.shadowOf(activity).getNextStartedActivity();
        assertEquals(expectedIntent.getComponent(), actualIntent.getComponent());
    }

    // Add more test methods here
}
