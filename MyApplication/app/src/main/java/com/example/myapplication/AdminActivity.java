package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {
    private Spinner spinnerReportedUsers;
    private TextView tvReportReason;
    private Button btnBanUser, btnRemoveFromList, btnSignOut;
    private List<String> reportedUsers;
    private List<String> reportReasons;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        spinnerReportedUsers = findViewById(R.id.spinnerReportedUsers);
        tvReportReason = findViewById(R.id.tvReportReason);
        btnBanUser = findViewById(R.id.btnBanUser);
        btnRemoveFromList = findViewById(R.id.btnRemoveFromList);
        btnSignOut = findViewById(R.id.btnSignOut);

        // Initialize reported users and reasons
        reportedUsers = new ArrayList<>();
        reportReasons = new ArrayList<>();

        // Setup the spinner adapter
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, reportedUsers);
        spinnerReportedUsers.setAdapter(adapter);

        // Fetch reported users from the database and populate the spinner
        fetchReportedUsers();

        // Set item click listener for the spinner
        spinnerReportedUsers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Update the reason for the report based on the selected user
                if (position >= 0 && position < reportReasons.size()) {
                    tvReportReason.setText("User was reported for: " + reportReasons.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Clear the report reason text
                tvReportReason.setText("User was reported for: ");
            }
        });

        // Set click listeners for buttons
        btnBanUser.setOnClickListener(v -> {
            int position = spinnerReportedUsers.getSelectedItemPosition();
            if (position >= 0 && position < reportedUsers.size()) {
                String userEmail = reportedUsers.get(position);
                banUser(userEmail);
            }
        });


        btnRemoveFromList.setOnClickListener(v -> {
            // Implement remove from list logic here
        });

        btnSignOut.setOnClickListener(v -> {
            // Implement sign out logic here
        });

    }

    private void fetchReportedUsers() {
        FirebaseFirestore.getInstance().collection("Reports")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        reportedUsers.clear();
                        reportReasons.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Get user email and reason from each report
                            String userEmail = document.getString("userEmail");
                            String reason = document.getString("reason");
                            if (userEmail != null && reason != null) {
                                // Add user email to reported users list
                                reportedUsers.add(userEmail);
                                // Add reason to report reasons list
                                reportReasons.add(reason);
                            }
                        }
                        // Notify adapter that data set has changed
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(AdminActivity.this, "Failed to fetch reported users", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void banUser(String userEmail) {
        FirebaseFirestore.getInstance().collection("users")
                .whereEqualTo("email", userEmail)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        // Delete the user's account document
                        String userId = documentSnapshot.getId();
                        FirebaseFirestore.getInstance().collection("users")
                                .document(userId)
                                .delete()
                                .addOnSuccessListener(aVoid -> {
                                    // User's account successfully deleted
                                    Toast.makeText(AdminActivity.this, "User banned successfully", Toast.LENGTH_SHORT).show();
                                    // Optionally, you can also remove the user from the reported users list
                                    reportedUsers.remove(userEmail);
                                    adapter.notifyDataSetChanged();
                                })
                                .addOnFailureListener(e -> {
                                    // Error handling if user's account deletion fails
                                    Toast.makeText(AdminActivity.this, "Failed to ban user", Toast.LENGTH_SHORT).show();
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    // Error handling if user query fails
                    Toast.makeText(AdminActivity.this, "Failed to ban user", Toast.LENGTH_SHORT).show();
                });
    }
}
