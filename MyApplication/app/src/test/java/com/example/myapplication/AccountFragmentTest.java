package com.example.wearwise;

import static org.junit.Assert.*;

import android.support.annotation.NonNull;

import com.example.wearwise.Account.AccountFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;

import java.util.Optional;


public class AccountFragmentTest {

    private String testUserID;
    private DatabaseReference ref;
    private AccountFragment af;
    private FirebaseDatabase database;

    private FragmentScenario<AccountFragment> scenario;

    @Before
    public void setUp() {
        testUserID = "testUserID"; //tester ID
        database = FirebaseDatabase.getInstance(); // instance of the database
        af = new AccountFragment(); // instance of the tested class

    }

    @Test
    public void newInstance() {
        AccountFragment fragment = AccountFragment.newInstance("param1", "param2");
        assertNotNull(fragment);
    }

    @Test
    public void loadStreak() {

        // Db instance with the proper path
        DatabaseReference streakRef = database.getReference("users").child(testUserID + "/streak");

        // calling the Account Fragment method to test
        af.loadStreak();

        // check if these are the same
        streakRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                assertEquals(Optional.of(0), dataSnapshot.getValue(Integer.class));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                fail(databaseError.getMessage());
            }
        });
    }

    @Test
    public void checkMemoryForDate() {

    }

    @Test
    public void getUserInfo() {
    }

    @Test
    public void loadProfilePicture() {
    }

    @Test
    public void loadPostCount() {
    }

    @Test
    public void testSaveAboutText() throws InterruptedException {
        String testAboutMe = "Test About Me";

        // Db instance with the proper path
        DatabaseReference aboutMeRef = database.getReference("tests").child( testUserID + "/about me");

        // calling the Account Fragment method to test, but in a different directory
        af.saveAboutText(testAboutMe, "tests");

        // check if these are the same
        aboutMeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                assertEquals(testAboutMe, dataSnapshot.getValue(String.class));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                fail(databaseError.getMessage());
            }
        });
    }

}