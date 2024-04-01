package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SimpleChat extends AppCompatActivity {

    private static final String TAG = "SimpleChat";
    private FirebaseFirestore db;
    private RecyclerView recyclerViewChat;
    private SingleChatAdapter chatAdapter;
    private List<ChatMessage> messageList;
    private String currentUserId;
    private EditText messageInput;
    private Button sendButton;
    private String chatId; // The unique ID for the chat
    private String receiverId; // The ID for the receiver of the messages

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simplechat);

        db = FirebaseFirestore.getInstance();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        messageList = new ArrayList<>();
        chatAdapter = new SingleChatAdapter(messageList, currentUserId);

        recyclerViewChat = findViewById(R.id.chat_messages_recycler_view);
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewChat.setAdapter(chatAdapter);

        messageInput = findViewById(R.id.chat_message_input);
        sendButton = findViewById(R.id.chat_send_button);

        // Retrieve the chat ID and receiver ID passed from AllChatsActivity
        chatId = getIntent().getStringExtra("CHAT_ID");
        receiverId = getIntent().getStringExtra("RECEIVER_ID");

        if (chatId == null || receiverId == null) {
            Log.e(TAG, "Chat ID or Receiver ID not provided.");
            finish(); // Exit if essential data is missing
            return;
        }

        sendButton.setOnClickListener(view -> sendMessage());

        fetchMessages();
    }

    private void sendMessage() {
        String messageText = messageInput.getText().toString().trim();
        if (!messageText.isEmpty()) {
            ChatMessage newMessage = new ChatMessage(
                    messageText,
                    true,
                    "0",
                    0,
                    currentUserId, // Sender ID
                    receiverId// Use the dynamically obtained receiver ID
                     // Timestamp
                     // userProfileImageId (if used)
            );

            db.collection("chats").document(chatId)
                    .collection("messages")
                    .add(newMessage)
                    .addOnSuccessListener(documentReference -> {
                        Log.d(TAG, "Message sent successfully");
                        messageInput.setText(""); // Clear the input field after sending
                    })
                    .addOnFailureListener(e -> Log.e(TAG, "Error sending message", e));
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
                            Log.e(TAG, "Error loading messages: ", e);
                            return;
                        }

                        messageList.clear();
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                            ChatMessage message = snapshot.toObject(ChatMessage.class);
                            messageList.add(message);
                        }
                        chatAdapter.notifyDataSetChanged();
                    }
                });
    }
}
