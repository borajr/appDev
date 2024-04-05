package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Handles the matching logic between users in an app, including recording swipes and checking for matches.
 */
public class MainMatch extends AppCompatActivity {

    private MainPage mainPage;

    /**
     * Constructor to create a MainMatch instance with a reference to MainPage for callbacks.
     *
     * @param mainPage The MainPage instance for callback invocation.
     */
    public MainMatch(MainPage mainPage){
        this.mainPage =  mainPage;
    }

    /**
     * Records a swipe action from one user towards another in the Firestore database.
     *
     * @param swiperEmail The email of the user who performed the swipe.
     * @param swipedEmail The email of the user who was swiped on.
     * @param direction   The direction of the swipe ("left" or "right").
     */
    public void recordSwipe(String swiperEmail, String swipedEmail, String direction) {
        if (swiperEmail.equals(swipedEmail)) {
            Log.w(TAG, "User attempted to swipe themselves, which is not allowed.");
            return; // Exit the method early
        }

        // Prepare the swipe data for saving.
        Map<String, Object> swipeData = new HashMap<>();
        swipeData.put("swiperEmail", swiperEmail);

        swipeData.put("swipedEmail", swipedEmail);
        swipeData.put("direction", direction);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Swipes").add(swipeData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                if ("right".equals(direction)) {
                    checkForMatch(swiperEmail, swiperEmail);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding swipe document", e);
            }
        });
    }


    /**
     * Checks if two users have mutually swiped right on each other, indicating a match.
     *
     * @param swiperEmail The email of the user who performed the swipe.
     * @param swipedEmail The email of the user who was swiped on.
     */
    public void checkForMatch(final String swiperEmail, final String swipedEmail) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Check if swiper has swiped right
        db.collection("Swipes")
                .whereEqualTo("swiperEmail", swiperEmail)
                .whereEqualTo("swipedEmail", swipedEmail)
                .whereEqualTo("direction", "right")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        // Check if swiped has also swiped right on swiper
                        db.collection("Swipes")
                                .whereEqualTo("swiperEmail", swipedEmail)
                                .whereEqualTo("swipedEmail", swiperEmail)
                                .whereEqualTo("direction", "right")
                                .get()
                                .addOnCompleteListener(task2 -> {
                                    if (task2.isSuccessful() && !task2.getResult().isEmpty()) {
                                        // Both users have swiped right on each other, create a match
                                        createMatch(swiperEmail, swipedEmail);
                                    } else {
                                        Log.d(TAG, "No reciprocal swipe found.");
                                    }
                                });
                    } else {
                        Log.d(TAG, "Swiper did not swipe right or error occurred: ", task.getException());
                    }
                });
    }

    /**
     * Creates a match between two users when a mutual right swipe is detected.
     *
     * @param userEmail1 Email of the first user involved in the match.
     * @param userEmail2 Email of the second user involved in the match.
     */
    private void createMatch(String userEmail1, String userEmail2) {
        Map<String, Object> matchData = new HashMap<>();
        matchData.put("user1Mail", userEmail1);
        matchData.put("user2Mail", userEmail2);
        matchData.put("timestamp", new Timestamp(new Date()));

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Matches").add(matchData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "Match created with ID: " + documentReference.getId());
                mainPage.showMatchPopup();
                // Here you can update UI or send notifications
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding match document", e);
            }
        });
    }



}