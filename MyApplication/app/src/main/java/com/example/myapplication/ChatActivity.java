package com.example.myapplication;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
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

        fetchMessages(conversationId);
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
}
