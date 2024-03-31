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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;

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
        sendButton.setOnClickListener(view -> sendMessage());

        fetchMessages();
    }

    private void sendMessage() {
        String messageText = messageInput.getText().toString().trim();
        if (!messageText.isEmpty()) {
            // Assuming the currentUserId is the sender's ID
            // The receiverUserId needs to be determined by your chat logic
            String receiverUserId = "RECEIVER_USER_ID"; // Replace with dynamic receiver ID based on your app logic

            // Constructing the new message
            ChatMessage newMessage = new ChatMessage(
                    messageText, // message text
                    true, // isSent - assuming true when sending
                    "timestamp_placeholder", // timestamp, consider formatting or using ServerValue.TIMESTAMP from Firebase
                    0, // userProfileImageId, adjust as necessary
                    currentUserId, // sender ID
                    receiverUserId // receiver ID
            );

            // Fetch the chatId, similar to how we retrieve it for fetching messages
            String chatId = getIntent().getStringExtra("CHAT_ID");
            if(chatId != null && !chatId.isEmpty()) {
                db.collection("chats").document(chatId)
                        .collection("messages")
                        .add(newMessage)
                        .addOnSuccessListener(documentReference -> {
                            Log.d(TAG, "Message sent successfully");
                            messageInput.setText(""); // Clear the input field
                        })
                        .addOnFailureListener(e -> Log.e(TAG, "Error sending message", e));
            } else {
                Log.e(TAG, "No chat ID specified.");
                // Optionally, show an error message to the user
            }
        } else {
            Log.e(TAG, "Attempted to send an empty message.");
            // Optionally, notify the user that they can't send an empty message
        }
    }


    private void fetchMessages() {
        String chatId = getIntent().getStringExtra("CHAT_ID");
        if (chatId == null || chatId.isEmpty()) {
            Log.e(TAG, "No chat ID provided.");
            finish(); // Close the activity if there's no chat ID
            return;
        }

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
                        recyclerViewChat.scrollToPosition(messageList.size() - 1);
                    }
                });
    }

    // Rest of your SimpleChat code...
}
