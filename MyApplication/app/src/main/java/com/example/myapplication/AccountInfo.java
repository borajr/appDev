package com.example.myapplication;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class AccountInfo extends AppCompatActivity {

    //onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        // Find the buttons or text views
        View btnMain = findViewById(R.id.btnmain);
        View btnSignOut = findViewById(R.id.btnSignOut);
        View btnHadiSiktirGit = findViewById(R.id.btnDelete);

        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start LoginActivity
                Intent intent = new Intent(AccountInfo.this, MainPage.class);
                startActivity(intent);
            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start LoginActivity
                Intent intent = new Intent(AccountInfo.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnHadiSiktirGit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get current user
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (user != null) {
                    String uid = user.getUid(); // Get user's uid to delete from Firestore

                    //Delete the user from Firebase Authentication
                    user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // User auth deletion is successful

                                // Now delete the user from Firestore 'users' collection
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                db.collection("users").document(uid)
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // DocumentSnapshot successfully deleted
                                                Toast.makeText(AccountInfo.this, "Your account has been deleted.", Toast.LENGTH_SHORT).show();

                                                // Take user back to the login screen or a confirmation screen
                                                Intent intent = new Intent(AccountInfo.this, MainActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clear the back stack
                                                startActivity(intent);
                                                finish(); // Ensure this activity is finished so user can't go back
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Handle the error
                                                Toast.makeText(AccountInfo.this, "Error deleting user data.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else {
                                // Handle if there is an error in deleting
                                Toast.makeText(AccountInfo.this, "Error deleting account.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(AccountInfo.this, "No user is signed in.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
