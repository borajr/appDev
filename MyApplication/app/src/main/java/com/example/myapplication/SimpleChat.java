package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.EventListener; // Correct import
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SimpleChat extends AppCompatActivity {

    private static final String TAG = "SimpleChat";
    private FirebaseFirestore db;
    private RecyclerView recyclerViewChat;
    private SingleChatAdapter chatAdapter;
    private List<ChatMessage> messageList;
    private EditText messageInput;
    private Button sendButton;
    private String chatId; // The unique ID for the chat
    private String receiverEmail; // The email of the receiver

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simplechat);

        db = FirebaseFirestore.getInstance();
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        messageList = new ArrayList<>();
        chatAdapter = new SingleChatAdapter(messageList, currentUserId);

        recyclerViewChat = findViewById(R.id.chat_messages_recycler_view);
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewChat.setAdapter(chatAdapter);

        messageInput = findViewById(R.id.chat_message_input);
        sendButton = findViewById(R.id.chat_send_button);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });


        fetchMessages();
    }

    private void sendMessage() {
        String senderEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail(); // Get sender's email
        String messageText = messageInput.getText().toString();
        if (!messageText.isEmpty()) {
            Map<String, Object> newMessage = new HashMap<>();
            newMessage.put("message", messageText);
            newMessage.put("senderEmail", senderEmail); // Use the sender's email
            newMessage.put("receiverEmail", receiverEmail); // Use the receiver's email
            newMessage.put("timestamp", System.currentTimeMillis()); // Use current timestamp

            db.collection("chats").document(chatId)
                    .collection("messages")
                    .add(newMessage)
                    .addOnSuccessListener(documentReference -> {
                        Log.d(TAG, "Message sent successfully");
                        messageInput.setText(""); // Clear the input after sending
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
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        List<ChatMessage> fetchedMessages = new ArrayList<>();
                        if (snapshots != null) {
                            for (DocumentSnapshot doc : snapshots.getDocuments()) {
                                ChatMessage message = doc.toObject(ChatMessage.class);
                                fetchedMessages.add(message);
                            }
                        }

                        messageList.clear();
                        messageList.addAll(fetchedMessages);
                        chatAdapter.notifyDataSetChanged();
                        // Scroll to the last message
                        if (fetchedMessages.size() > 0) {
                            recyclerViewChat.scrollToPosition(fetchedMessages.size() - 1);
                        }
                    }
                });
    }
}
