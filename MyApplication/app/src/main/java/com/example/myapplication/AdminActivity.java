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

public class AdminActivity extends AppCompatActivity {
    private Spinner spinnerReportedUsers;
    private OrientationEventListener orientationEventListener;

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

        // Add dummy reported users and reasons


        // Setup the spinner adapter
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, reportedUsers);
        spinnerReportedUsers.setAdapter(adapter);

        fetchReportedUsers();

        spinnerReportedUsers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Display the reason for the report
                if (position >= 0 && position < reportReasons.size()) {
                    tvReportReason.setText("User was reported for: " + reportReasons.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Clear the report reason text
                tvReportReason.setText("User was reported for: ");
            }
        });

        btnBanUser.setOnClickListener(v -> {
            int position = spinnerReportedUsers.getSelectedItemPosition();
            if (position >= 0 && position < reportedUsers.size()) {
                Toast.makeText(AdminActivity.this, "User will be removed in 10 business days", Toast.LENGTH_SHORT).show();
                return;
            }
        });

        btnRemoveFromList.setOnClickListener(v -> {
            int position = spinnerReportedUsers.getSelectedItemPosition();
            if (position < 0) {
                Toast.makeText(AdminActivity.this, "Please select a user", Toast.LENGTH_SHORT).show();
                return;
            }
            String userEmail = reportedUsers.get(position);
            deleteReportForUser(userEmail);
            // After deleting the report, remove the user from the spinner
            reportedUsers.remove(position);
            reportReasons.remove(position);
            adapter.notifyDataSetChanged();
            Toast.makeText(AdminActivity.this, userEmail + " is removed from the list", Toast.LENGTH_SHORT).show();
        });

        btnSignOut.setOnClickListener(v -> {
            // Sign out logic goes here
            Intent intent = new Intent(AdminActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        orientationEventListener = new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int orientation) {
                if (orientation >= 45 && orientation < 135) {
                    // Landscape mode, set screen orientation to reverse portrait
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else if (orientation >= 135 && orientation < 225) {
                    // Upside down mode, set screen orientation to portrait
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
                } else if (orientation >= 225 && orientation < 315) {
                    // Reverse landscape mode, set screen orientation to portrait
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
                } else {
                    // Portrait mode, set screen orientation to portrait
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
            }
        };

        // Start the OrientationEventListener
        orientationEventListener.enable();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Disable the OrientationEventListener to prevent memory leaks
        orientationEventListener.disable();
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



    private void deleteReportForUser(String userEmail) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Reports")
                .whereEqualTo("userEmail", userEmail)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot reportDoc : queryDocumentSnapshots) {
                        // Delete the report document
                        db.collection("Reports").document(reportDoc.getId()).delete()
                                .addOnSuccessListener(aVoid -> {
                                    // Report successfully deleted
                                    Toast.makeText(AdminActivity.this, "Report removed successfully", Toast.LENGTH_SHORT).show();
                                    // Remove the user from the spinner
                                    reportedUsers.remove(userEmail);
                                    reportReasons.remove(spinnerReportedUsers.getSelectedItemPosition());
                                    adapter.notifyDataSetChanged();
                                })
                                .addOnFailureListener(e -> Toast.makeText(AdminActivity.this, "Failed to remove report", Toast.LENGTH_SHORT).show());
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(AdminActivity.this, "Failed to find report", Toast.LENGTH_SHORT).show());
    }



}
