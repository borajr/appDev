package com.example.myapplication;

// Importing necessary Android and Firebase libraries
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
    // Tag used for logging. It's a good practice to tag your log entries for easier debugging.
    private static final String TAG = "ChatActivity";

    // UI components and adapters
    private RecyclerView recyclerViewChat;
    private SingleChatAdapter chatAdapter;

    // Data model
    private List<ChatMessage> messageList;
    private FirebaseFirestore db;
    private String currentUser;
    private String otherUser; // The ID of the other user in the chat

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simplechat); // Setting the layout for the activity

        // Initialize Firestore and authentication instances
        db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUse = FirebaseAuth.getInstance().getCurrentUser();

        // Set the user IDs. Normally, these would be determined based on the chat selected or logged-in user info.
        if (currentUse != null) {
            currentUser = currentUse.getEmail();
            otherUser = "OTHER_USER_ID"; // Placeholder - should be set dynamically based on selected chat
        } else {
            // If no user is logged in, log error and exit the activity
            Log.e(TAG, "No user logged in!");
            finish();
            return;
        }

        // Initialize the message list and set up the chat adapter
        messageList = new ArrayList<>();
        chatAdapter = new SingleChatAdapter(messageList, currentUser);

        // Configure the RecyclerView - setting the layout manager and attaching the adapter
        recyclerViewChat = findViewById(R.id.chat_messages_recycler_view);
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewChat.setAdapter(chatAdapter);

        // Get chat details passed from the previous activity/screen
        Intent intent = getIntent();
        String conversationId = intent.getStringExtra("CHAT_ID");
        otherUser = intent.getStringExtra("RECEIVER_ID");

        // Check for necessary chat details, log error and exit if missing
        if (conversationId == null || otherUser == null) {
            Log.e(TAG, "No conversation ID or other user's email found.");
            finish();
            return;
        }

        // Load messages for the given conversation
        fetchMessages(conversationId);
    }

    private void fetchMessages(String conversationId) {
        // Fetch messages from Firestore based on conversationId. Assume the id comes from the intent extras.
        db.collection("messages")
                .whereEqualTo("conversationId", conversationId)
                .orderBy("timestamp") // Assuming there's a timestamp field for sorting messages
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            // Log any errors during the fetch operation
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        // Process fetched messages and update UI
                        List<ChatMessage> fetchedMessages = new ArrayList<>();
                        for (DocumentSnapshot doc : queryDocumentSnapshots) {
                            ChatMessage message = doc.toObject(ChatMessage.class);
                            fetchedMessages.add(message);
                        }

                        // Update the local list and refresh the UI
                        messageList.clear();
                        messageList.addAll(fetchedMessages);
                        chatAdapter.notifyDataSetChanged(); // Notify the adapter to redraw the RecyclerView
                    }
                });
    }
}
