package com.example.myapplication;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleChat extends AppCompatActivity {

    private static final String TAG = "SimpleChat";
    private FirebaseFirestore db;
    private RecyclerView recyclerViewChat;
    private SingleChatAdapter chatAdapter;
    private List<ChatMessage> messageList;
    private String currentUserId;
    private String chatPartnerId;
    private EditText messageInput;
    private Button sendButton;

    private ChatActivity chatActivity;


    private ImageView burgerMenu;
    private String chatId; // The unique ID for the chat

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simplechat);

        ImageView menuButton = findViewById(R.id.chat_menu_button);
        menuButton.setOnClickListener(view -> PopupUtils.showMenuPopup(this, this::onUnmatch, this::onReport));
        db = FirebaseFirestore.getInstance();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        messageList = new ArrayList<>();
        chatAdapter = new SingleChatAdapter(messageList, currentUserId);

        recyclerViewChat = findViewById(R.id.chat_messages_recycler_view);
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewChat.setAdapter(chatAdapter);


        messageInput = findViewById(R.id.chat_message_input);
        sendButton = findViewById(R.id.chat_send_button);
        sendButton.setOnClickListener(view -> sendMessage());

        // Retrieve the chat ID passed from AllChatsActivity
        chatId = getIntent().getStringExtra("CHAT_ID");
        if (chatId != null && !chatId.isEmpty()) {
            fetchChatPartnerId();
            fetchMessages();
        } else {
            Log.e(TAG, "No chat ID provided.");
            finish(); // Optionally, exit if there's no chat ID
        }
    }


    public void onUnmatch() {
        // First delete the chat document from 'chats' collection
        deleteChatDocument(chatId, () -> {
            // Then query and delete the match document
            queryAndDeleteMatch(currentUserId, chatPartnerId);
        });
    }

    // Helper method to delete chat document
    private void deleteChatDocument(String chatId, Runnable onSuccessCallback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("chats").document(chatId)
                .delete()
                .addOnSuccessListener(unused -> {
                    Log.d(TAG, "Chat document successfully deleted");
                    onSuccessCallback.run(); // Run the callback on success
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error deleting chat document", e));
    }

    // Helper method to query and delete match document
    private void queryAndDeleteMatch(String currentUserEmail, String partnerEmail) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Query for the match document with currentUserEmail as user1Mail and partnerEmail as user2Mail
        db.collection("Matches")
                .whereIn("user1Mail", Arrays.asList(currentUserEmail, partnerEmail))
                .whereIn("user2Mail", Arrays.asList(currentUserEmail, partnerEmail))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            // Ensure the correct match is being deleted
                            String user1Mail = document.getString("user1Mail");
                            String user2Mail = document.getString("user2Mail");
                            if ((user1Mail.equals(currentUserEmail) && user2Mail.equals(partnerEmail)) ||
                                    (user1Mail.equals(partnerEmail) && user2Mail.equals(currentUserEmail))) {
                                // Delete the match document
                                db.collection("Matches").document(document.getId()).delete()
                                        .addOnSuccessListener(unused -> {
                                            Log.d(TAG, "Match document successfully deleted");
                                            transitionToMainActivity(); // Redirect to MainPage after deleting the match
                                        })
                                        .addOnFailureListener(e -> Log.w(TAG, "Error deleting match document", e));
                            }
                        }
                    } else {
                        Log.d(TAG, "Error getting match documents: ", task.getException());
                    }
                });
    }


    private void sendMessage() {
        String messageText = messageInput.getText().toString().trim();
        if (!messageText.isEmpty() && chatId != null) {
            ChatMessage newMessage = new ChatMessage(
                    messageText, // Message text
                    true, // isSent
                    String.valueOf(System.currentTimeMillis()), // Current time as a string for the timestamp
                    0, // userProfileImageId, adjust this as necessary
                    currentUserId, // Sender ID
                    "RECEIVER_USER_ID" // Placeholder for receiver ID, replace as needed
            );

            db.collection("chats").document(chatId)
                    .collection("messages")
                    .add(newMessage)
                    .addOnSuccessListener(documentReference -> {
                        Log.d(TAG, "Message sent successfully");
                        messageInput.setText(""); // Clear the input field
                    })
                    .addOnFailureListener(e -> Log.e(TAG, "Error sending message", e));
        } else {
            Log.e(TAG, "Message text is empty or chat ID is missing.");
        }
    }

    private void fetchMessages() {
        db.collection("chats").document(chatId)
                .collection("messages")
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
                        recyclerViewChat.scrollToPosition(messageList.size() - 1); // Auto-scroll to the latest message
                    }
                });
    }

    private void onReport() {
        View reportPopupView = LayoutInflater.from(SimpleChat.this).inflate(R.layout.report_pop_up, null);
        AlertDialog.Builder reportDialogBuilder = new AlertDialog.Builder(SimpleChat.this);
        reportDialogBuilder.setView(reportPopupView);
        AlertDialog reportDialog = reportDialogBuilder.create();
        reportDialog.setCanceledOnTouchOutside(true);
        EditText reasonEditText = reportPopupView.findViewById(R.id.edittext_report_reason); // Ensure this ID matches your layout
        reportPopupView.findViewById(R.id.button_send_report).setOnClickListener(v -> {
            // Here you can handle sending the report reason to your server or save it
            String reportReason = reasonEditText.getText().toString().trim();
            if (!reportReason.isEmpty()) {
                saveReportToFirestore(reportReason);
                Toast.makeText(SimpleChat.this, "Report sent for: " + reportReason, Toast.LENGTH_SHORT).show();
                onUnmatch();
            } else {
                Toast.makeText(SimpleChat.this, "Please enter a reason for reporting.", Toast.LENGTH_SHORT).show();
            }
            reportDialog.dismiss();
            transitionToMainActivity(); // Transition to the main activity after reporting
        });

        reportDialog.show();
    }

    private void transitionToMainActivity() {
        // Transition to the main activity
        Intent intent = new Intent(SimpleChat.this, MainPage.class);
        startActivity(intent);
        finish(); // Close the current activity
    }

    // Assume 'currentUserId' is the ID of the signed-in user
// 'chatPartnerId' is the ID of the user they are chatting with
    /*
    public void onUnmatch() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String partner = chatPartnerId;
        // Query for the match document
        Task<QuerySnapshot> querySnapshotTask = db.collection("Matches")
                .whereEqualTo("user1Mail", currentUserId)
                .whereEqualTo("user2Mail", partner)
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
                                                transitionToMainActivity();
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
    */
    private void fetchChatPartnerId() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Assuming the chat document contains a field called "participantEmails" which is a list
        db.collection("chats").document(chatId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        DocumentSnapshot chatDocument = task.getResult();
                        List<String> participantEmails = (List<String>) chatDocument.get("participantEmails");
                        if (participantEmails != null && participantEmails.size() == 2) {
                            for (String email : participantEmails) {
                                if (!email.equals(currentUserId)) {
                                    chatPartnerId = email;
                                    break;
                                }
                            }
                            if (chatPartnerId == null) {
                                Log.e(TAG, "Chat partner not found.");
                                finish(); // Exit if the chat partner ID is not found
                            }
                        } else {
                            Log.e(TAG, "Unexpected number of participants found or participantEmails is null.");
                            finish(); // Exit if the structure is not as expected
                        }
                    } else {
                        Log.e(TAG, "Error getting chat document: ", task.getException());
                        finish(); // Exit if there is an error fetching the chat document
                    }
                });
    }

    private void saveReportToFirestore(String reportReason) {
        // Get the current user's email
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        fetchChatPartnerId();
        // Create a map with the report data
        Map<String, Object> reportData = new HashMap<>();
        reportData.put("userEmail", chatPartnerId);
        reportData.put("reason", reportReason);
        reportData.put("timestamp", FieldValue.serverTimestamp());

        // Add the report to the "Reports" collection in Firestore
        FirebaseFirestore.getInstance().collection("Reports")
                .add(reportData)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Report added successfully");
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error adding report", e);
                });
    }

}