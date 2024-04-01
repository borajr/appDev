package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    private ChatAdapter chatAdapter;
    private ArrayList<chat> chatList; // Assuming 'chat' is your model class
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_chats);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Assuming the user is already logged in

        recyclerViewChats = findViewById(R.id.recyclerView_chats);
        recyclerViewChats.setLayoutManager(new LinearLayoutManager(this));

        chatList = new ArrayList<>();
        // Initialize with your static chat data using a method that checks for user participation
        addStaticChatIfParticipant("User1", "Hello", "12:00", R.drawable.ic_profile_placeholder, "BTxyVjbfF8Y4TKmHmBvmgOPIdKb2", "mEjYhiMTzuSHtkV73HkYPSvrjD93");
        addStaticChatIfParticipant("User2", "How are you?", "12:05", R.drawable.ic_profile_placeholder, "mEjYhiMTzuSHtkV73HkYPSvrjD93", "BTxyVjbfF8Y4TKmHmBvmgOPIdKb2");

        chatAdapter = new ChatAdapter(chatList, this); // Assuming ChatAdapter is implemented to handle your 'chat' model class
        recyclerViewChats.setAdapter(chatAdapter);

        fetchDynamicChats();
    }

    private void addStaticChatIfParticipant(String userName, String lastMessage, String timestamp, int profilePic, String senderId, String receiverId) {
        // Add the static chat only if the current user is either the sender or receiver
        if (currentUserId.equals(senderId) || currentUserId.equals(receiverId)) {
            chatList.add(new chat(userName, lastMessage, timestamp, profilePic, senderId, receiverId));
        }
    }

    private void fetchDynamicChats() {
        db.collection("chats")
                .whereArrayContains("participantIds", currentUserId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Extract chat data from each document
                            String userName = document.getString("userName"); // Adjust field names based on your Firestore structure
                            String lastMessage = document.getString("lastMessage"); // Example field
                            String timestamp = document.getString("timestamp"); // Example field
                            String receiverId = document.getString("receiverId"); // Assuming this field exists in your Firestore documents

                            chatList.add(new chat(userName, lastMessage, timestamp, R.drawable.ic_profile_placeholder, currentUserId, receiverId));
                        }
                        chatAdapter.notifyDataSetChanged();
                    } else {
                        Log.w(TAG, "Error fetching dynamic chats.", task.getException());
                    }
                });
    }

    @Override
    public void onChatClicked(chat chat) {
        Intent intent = new Intent(AllChatsActivity.this, SimpleChat.class);
        intent.putExtra("CHAT_ID", chat.getUserName()); // Assuming you modify your chat model to include a getChatId method
        intent.putExtra("RECEIVER_ID", chat.getRecId()); // Ensure getReceiverId method exists and returns the correct ID
        startActivity(intent);
    }
}
