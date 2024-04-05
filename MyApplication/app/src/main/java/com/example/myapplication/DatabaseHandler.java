package com.example.myapplication;

import android.content.res.Resources;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles interactions with the Firebase Firestore database, including adding and updating user and preferences data.
 */
public class DatabaseHandler {
    // Firebase Authentication instance
    FirebaseAuth mAuth;
    // Tag for logging
    private static final String TAG = "DatabaseHandler";

    // Firebase Firestore database instance
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * Adds a new user to the Firestore database.
     * @param email The email of the user to add.
     */
    public void addUser(String email) {
        // User data to add
        Map<String,Object> user = new HashMap<>();
        user.put("banned", false);
        user.put("email", email);
        boolean success = false;
        // Add user to Firestore
        db.collection("users")
                .document(email)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Log success
                        Log.d(TAG, "DocumentSnapshot successfully written!");

                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Log failure
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    /**
     * Updates an existing user's data in Firestore.
     * @param data The data to update for the user.
     */
    public void updateUser(Map<String, Object> data) {
        // Initialize FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();
        String email = mAuth.getCurrentUser().getEmail();
        // Check if the user is a student or staff and update accordingly
        if (email.contains("student.tue.nl")) {
            // Update student user data
            db.collection("users")
                    .document(email)
                    .update(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Log success
                            Log.d(TAG, "DocumentSnapshot successfully written!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Log failure
                            Log.w(TAG, "Error writing document", e);
                        }
                    });

        } else {
            // Update staff user data
            db.collection("users_staff")
                    .document(email)
                    .update(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully written!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error writing document", e);
                        }
                    });
        }
    }

    /**
     * Creates a new user preferences document in Firestore.
     * @param data The preferences data to store.
     */
    public void createUserPref(Map<String, Object> data) {
        // Initialize FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();
        String email = mAuth.getCurrentUser().getEmail();
        // Attempt to set user preferences
        try {
            db.collection("preferences")
                    .document(email)
                    .set(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully written!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error writing document", e);
                        }
                    });
        } catch (Resources.NotFoundException e) {
            db.collection("preferences")
                    .document(email)
                    .set(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully written!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error writing document", e);
                        }
                    });
        }
    }

    /**
     * Updates an existing user preferences document in Firestore.
     * @param data The preferences data to update.
     */
    public void updateUserPref(Map<String, Object> data) {
        mAuth = FirebaseAuth.getInstance();
        String email = mAuth.getCurrentUser().getEmail();
        // Update user preferences
        db.collection("preferences")
                .document(email)
                .update(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }
}