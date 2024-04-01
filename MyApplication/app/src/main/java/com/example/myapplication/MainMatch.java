package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
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

public class MainMatch extends AppCompatActivity {

    public void recordSwipe(String swiperEmail, String swipedEmail, String direction) {
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

    private void checkForMatch(final String swiperEmail, final String swipedEmail) { //efranin gotu kocaman izmitin yollari tastan
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Swipes")
                .whereEqualTo("swiperEmail", swiperEmail)
                .whereEqualTo("swipedEmail", swipedEmail)
                .whereEqualTo("direction", "right")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.exists()) {
                                    createMatch(swiperEmail, swiperEmail);
                                    break;
                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

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
