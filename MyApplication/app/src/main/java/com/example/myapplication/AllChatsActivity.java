package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;

public class AllChatsActivity extends AppCompatActivity implements ChatAdapter.OnChatClickListener {
    private static final String TAG = "AllChatsActivity";
    private RecyclerView recyclerViewChats;
    private TextView placeholderTextView;
    private ChatAdapter chatAdapter;
    private ArrayList<chat> chatList; // Assuming 'chat' is your model class
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String currentUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_chats);


        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getEmail(); // Assuming the user is already logged in

        placeholderTextView = findViewById(R.id.placeholder_text_view);

        recyclerViewChats = findViewById(R.id.recyclerView_chats);
        recyclerViewChats.setLayoutManager(new LinearLayoutManager(this));

        chatList = new ArrayList<>();
        // Initialize with your static chat data using a method that checks for user participation
        addStaticChatIfParticipant("User1", "Hello", "12:00", R.drawable.ic_profile_placeholder, "User1", "boram@student.tue.nl", "borambi@student.tue.nl");
        addStaticChatIfParticipant("User2", "How are you?", "12:05", R.drawable.ic_profile_placeholder,  "User1", "borambi@student.tue.nl", "boram@student.tue.nl");

        chatAdapter = new ChatAdapter(chatList, this); // Assuming ChatAdapter is implemented to handle your 'chat' model class
        recyclerViewChats.setAdapter(chatAdapter);

        fetchDynamicChats();
    }

    private void addStaticChatIfParticipant(String userName, String lastMessage, String timestamp, int profilePic, String chatID, String senderEmail, String receiverEmail) {
        // Add the static chat only if the current user is either the sender or receiver
        if (currentUserId.equals(senderEmail) || currentUserId.equals(receiverEmail)) {
            chatList.add(new chat(userName, lastMessage, timestamp, profilePic, chatID, senderEmail, receiverEmail));
        }
    }

    private void fetchDynamicChats() {
        // Assuming 'participantEmails' is the field that contains the array of emails
        db.collection("chats")
                .whereArrayContains("participantEmails", currentUserId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Here you would extract and use the chat data as needed.
                            // You might need to adjust this if the usernames are not stored
                            // directly under the chat document.
                            String userName = document.getId(); // Using the document ID as the chat ID.
                            ArrayList<String> participantEmails = (ArrayList<String>) document.get("participantEmails");
                            String lastMessage = "Last message here"; // Placeholder for last message
                            String timestamp = "timestamp here"; // Placeholder for timestamp

                            // Check if the current user's email is in the participants' list
                            if (participantEmails.contains(currentUserId)) {
                                // The current user is a participant in this chat.
                                // Add the chat to the chat list.
                                chatList.add(new chat(userName, lastMessage, timestamp, R.drawable.ic_profile_placeholder, "", "", "")); // Replace with actual sender and receiver IDs
                            }
                        }
                        chatAdapter.notifyDataSetChanged();
                        updatePlaceholderVisibility();
                    } else {
                        Log.w(TAG, "Error fetching dynamic chats.", task.getException());
                    }
                });
    }

    private void updatePlaceholderVisibility() {
        if (chatList.isEmpty()) {
            placeholderTextView.setVisibility(View.VISIBLE);
        } else {
            placeholderTextView.setVisibility(View.GONE);
        }
    }


    @Override
    public void onChatClicked(chat chat) {
        Intent intent = new Intent(AllChatsActivity.this, SimpleChat.class);
        intent.putExtra("CHAT_ID", chat.getUserName()); // Assuming you modify your chat model to include a getChatId method
        intent.putExtra("RECEIVER_ID", chat.getReceiverEmail()); // Ensure getReceiverId method exists and returns the correct ID
        startActivity(intent);
    }
}
