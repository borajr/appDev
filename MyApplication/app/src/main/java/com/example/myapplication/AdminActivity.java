package com.example.myapplication;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

// Class representing the admin interface for managing reported users
public class AdminActivity extends AppCompatActivity {
    // Declare UI components
    private Spinner spinnerReportedUsers;
    private TextView tvReportReason;
    private Button btnBanUser, btnRemoveFromList, btnSignOut;

    // Lists to hold reported users and their report reasons
    private List<String> reportedUsers;
    private List<String> reportReasons;

    // Adapter for spinner (dropdown menu)
    private ArrayAdapter<String> adapter;

    // OrientationEventListener to handle screen orientation changes
    private OrientationEventListener orientationEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin); // Set layout

        // Initialize UI components
        spinnerReportedUsers = findViewById(R.id.spinnerReportedUsers);
        tvReportReason = findViewById(R.id.tvReportReason);
        btnBanUser = findViewById(R.id.btnBanUser);
        btnRemoveFromList = findViewById(R.id.btnRemoveFromList);
        btnSignOut = findViewById(R.id.btnSignOut);

        // Initialize lists for users and reasons
        reportedUsers = new ArrayList<>();
        reportReasons = new ArrayList<>();

        // Initialize spinner adapter
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, reportedUsers);
        spinnerReportedUsers.setAdapter(adapter);

        // Fetch reported users from database
        fetchReportedUsers();

        // Set listener for spinner selection changes
        spinnerReportedUsers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Update report reason text based on selected user
                if (position >= 0 && position < reportReasons.size()) {
                    tvReportReason.setText("User was reported for: " + reportReasons.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Clear report reason text if no user is selected
                tvReportReason.setText("User was reported for: ");
            }
        });

        // Set listener for ban user button
        btnBanUser.setOnClickListener(v -> {
            int position = spinnerReportedUsers.getSelectedItemPosition();
            if (position >= 0 && position < reportedUsers.size()) {
                // Show toast message for banning user
                Toast.makeText(AdminActivity.this, "User will be removed in 10 business days", Toast.LENGTH_SHORT).show();
            }
        });

        // Set listener for remove from list button
        btnRemoveFromList.setOnClickListener(v -> {
            int position = spinnerReportedUsers.getSelectedItemPosition();
            if (position < 0) {
                // If no user is selected, show toast message
                Toast.makeText(AdminActivity.this, "Please select a user", Toast.LENGTH_SHORT).show();
                return;
            }
            // Get email of selected user and delete their report
            String userEmail = reportedUsers.get(position);
            deleteReportForUser(userEmail);
            // Show toast message for removing user from list
            Toast.makeText(AdminActivity.this, userEmail + " is removed from the list", Toast.LENGTH_SHORT).show();
        });

        // Set listener for sign out button
        btnSignOut.setOnClickListener(v -> {
            // Navigate to login screen and finish current activity
            Intent intent = new Intent(AdminActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        // Initialize and enable OrientationEventListener
        orientationEventListener = new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int orientation) {
                // Adjust screen orientation based on device orientation
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        };
        orientationEventListener.enable();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Disable orientation listener to prevent memory leaks
        orientationEventListener.disable();
    }

    // Fetches reported users and their reasons from database
    private void fetchReportedUsers() {
        FirebaseFirestore.getInstance().collection("Reports")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        reportedUsers.clear();
                        reportReasons.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Extract user email and report reason
                            String userEmail = document.getString("userEmail");
                            String reason = document.getString("reason");
                            if (userEmail != null && reason != null) {
                                reportedUsers.add(userEmail);
                                reportReasons.add(reason);
                            }
                        }
                        // Notify adapter of data change
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(AdminActivity.this, "Failed to fetch reported users", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Deletes the report for a specified user
    private void deleteReportForUser(String userEmail) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Reports")
                .whereEqualTo("userEmail", userEmail)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot reportDoc : queryDocumentSnapshots) {
                        // Delete each report document
                        db.collection("Reports").document(reportDoc.getId()).delete()
                                .addOnSuccessListener(aVoid -> Toast.makeText(AdminActivity.this, "Report removed successfully", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(AdminActivity.this, "Failed to remove report", Toast.LENGTH_SHORT).show());
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(AdminActivity.this, "Failed to find report", Toast.LENGTH_SHORT).show());
    }
}
