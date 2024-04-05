package com.example.myapplication;

import android.content.Intent;
import android.content.pm.ActivityInfo;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {28})
public class MainActivityTest {

    private MainActivity mainActivity;

    @Before
    public void setUp() {
        mainActivity = Robolectric.setupActivity(MainActivity.class);
    }

    @Test
    public void clickingLogin_shouldStartLoginActivity() {
        mainActivity.findViewById(R.id.button2).performClick();

        Intent expectedIntent = new Intent(mainActivity, LoginActivity.class);
        ShadowActivity shadowActivity = Shadows.shadowOf(mainActivity);
        Intent actualIntent = shadowActivity.getNextStartedActivity();

        assertNotNull(actualIntent);
        assertEquals(expectedIntent.getComponent(), actualIntent.getComponent());
    }

    @Test
    public void clickingSignUp_shouldStartSignupActivity() {
        mainActivity.findViewById(R.id.button3).performClick();

        Intent expectedIntent = new Intent(mainActivity, SignupActivity.class);
        ShadowActivity shadowActivity = Shadows.shadowOf(mainActivity);
        Intent actualIntent = shadowActivity.getNextStartedActivity();

        assertNotNull(actualIntent);
        assertEquals(expectedIntent.getComponent(), actualIntent.getComponent());
    }

    @Test
    public void orientationChange_shouldNotCrashApp() {
        mainActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        assertNotNull(mainActivity);
    }
}
