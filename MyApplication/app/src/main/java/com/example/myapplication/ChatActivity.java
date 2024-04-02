package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private static final String TAG = "ChatActivity";
    private RecyclerView recyclerViewChat;
    private SingleChatAdapter chatAdapter;
    private List<ChatMessage> messageList;
    private FirebaseFirestore db;
    private String currentUser;
    private String otherUser; // The ID of the other user in the chat

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simplechat);

        db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUse = FirebaseAuth.getInstance().getCurrentUser();

        // Set the user IDs. In a real app, these would be determined based on the chat selected.
        if (currentUser != null) {
            currentUser = currentUse.getEmail();
            // For demonstration purposes, we're using a fixed otherUserId.
            otherUser = "OTHER_USER_ID"; // TODO: Set this based on the chat selected.
        } else {
            // Handle the case where the user is not logged in (this shouldn't happen in this activity)
            Log.e(TAG, "No user logged in!");
            finish(); // Exit the ChatActivity
            return;
        }

        // Initialize the message list and adapter
        messageList = new ArrayList<>();
        chatAdapter = new SingleChatAdapter(messageList, currentUser);

        // Setup RecyclerView
        recyclerViewChat = findViewById(R.id.chat_messages_recycler_view);
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewChat.setAdapter(chatAdapter);

        Intent intent = getIntent();
        String conversationId = intent.getStringExtra("CHAT_ID");
        otherUser = intent.getStringExtra("RECEIVER_ID");

        if (conversationId == null || otherUser == null) {
            Log.e(TAG, "No conversation ID or other user's email found.");
            finish(); // Finish activity if required data is not available
            return;
        }

        // Fetch messages using the retrieved conversation ID
        fetchMessages(conversationId);

        // Setup the menu button
        ImageView menuButton = findViewById(R.id.chat_menu_button);
        menuButton.setOnClickListener(view -> showMenuPopup());

        // TODO: Setup the send button and message input
        // You will need to add code here that sends a message when the send button is clicked
    }

    private void fetchMessages(String conversationId) {
        // Assuming you have a conversationId that you have received from the previous screen (chat list)

        db.collection("messages")
                .whereEqualTo("conversationId", conversationId)
                .orderBy("timestamp")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        List<ChatMessage> fetchedMessages = new ArrayList<>();
                        for (DocumentSnapshot doc : queryDocumentSnapshots) {
                            ChatMessage message = doc.toObject(ChatMessage.class);
                            fetchedMessages.add(message);
                        }

                        messageList.clear();
                        messageList.addAll(fetchedMessages);
                        chatAdapter.notifyDataSetChanged();
                    }
                });
    }

    // ... Existing methods for showing pop-ups and transitioning to main activity ...

    private void showMenuPopup() {
        View popupView = LayoutInflater.from(ChatActivity.this).inflate(R.layout.chat_pop_up_menu, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ChatActivity.this);
        alertDialogBuilder.setView(popupView);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(true);

        // Handle "Unmatch" button click
        popupView.findViewById(R.id.button_unmatch).setOnClickListener(v -> {
            // Add logic to handle unmatching here, if needed
            transitionToMainActivity();
            alertDialog.dismiss();
        });

        // Handle "Report" button click
        popupView.findViewById(R.id.button_report).setOnClickListener(v -> {
            alertDialog.dismiss();
            showReportPopup();
        });

        alertDialog.show();
    }

    private void showReportPopup() {
        View reportPopupView = LayoutInflater.from(ChatActivity.this).inflate(R.layout.report_pop_up, null);
        AlertDialog.Builder reportDialogBuilder = new AlertDialog.Builder(ChatActivity.this);
        reportDialogBuilder.setView(reportPopupView);
        AlertDialog reportDialog = reportDialogBuilder.create();
        reportDialog.setCanceledOnTouchOutside(true);
        EditText reasonEditText = reportPopupView.findViewById(R.id.edittext_report_reason); // Ensure this ID matches your layout
        reportPopupView.findViewById(R.id.button_send_report).setOnClickListener(v -> {
            // Here you can handle sending the report reason to your server or save it
            String reportReason = reasonEditText.getText().toString().trim();
            if (!reportReason.isEmpty()) {
                // TODO: Send report reason to your backend/server
                Toast.makeText(ChatActivity.this, "Report sent for: " + reportReason, Toast.LENGTH_SHORT).show();
                unmatchUser(currentUser, otherUser);
            } else {
                Toast.makeText(ChatActivity.this, "Please enter a reason for reporting.", Toast.LENGTH_SHORT).show();
            }
            reportDialog.dismiss();
            transitionToMainActivity(); // Transition to the main activity after reporting
        });

        reportDialog.show();
    }

    private void transitionToMainActivity() {
        // Transition to the main activity
        Intent intent = new Intent(ChatActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Close the current activity
    }

    // Assume 'currentUserId' is the ID of the signed-in user
// 'chatPartnerId' is the ID of the user they are chatting with
    public void unmatchUser(String currentUserId, String chatPartnerId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Query for the match document
        Task<QuerySnapshot> querySnapshotTask = db.collection("Matches")
                .whereEqualTo("user1Mail", currentUserId)
                .whereEqualTo("user2Mail", chatPartnerId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                // Delete the match document
                                db.collection("Matches").document(document.getId()).delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // Redirect to MainPage
                                                Intent intent = new Intent(ChatActivity.this, MainPage.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Handle failure
                                                Log.w(TAG, "Error deleting match document", e);
                                            }
                                        });
                            }
                        } else {
                            Log.d(TAG, "Error getting match documents: ", task.getException());
                        }
                    }
                });
    }
}
