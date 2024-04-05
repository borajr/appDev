package com.example.myapplication;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Displays all chats for the current user, handling creation and retrieval of chat documents
 * from Firestore based on matches.
 */
public class AllChatsActivity extends AppCompatActivity implements ChatAdapter.OnChatClickListener {
    private static final String TAG = "AllChatsActivity";
    private RecyclerView recyclerViewChats;
    private TextView placeholderTextView;
    private ChatAdapter chatAdapter;
    private ArrayList<chat> chatList; // Assuming 'chat' is your model class
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String currentUserId;
    private OrientationEventListener orientationEventListener;

    // Counters for handling chat creation based on matches
    private AtomicInteger matchCount = new AtomicInteger(0);
    private AtomicInteger processedMatches = new AtomicInteger(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_chats);

        // Initialize the current user ID from FirebaseAuth
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getEmail(); // Assuming the user is already logged in

        // Setup RecyclerView and adapter for displaying chats
        placeholderTextView = findViewById(R.id.placeholder_text_view);

        recyclerViewChats = findViewById(R.id.recyclerView_chats);
        recyclerViewChats.setLayoutManager(new LinearLayoutManager(this));

        chatList = new ArrayList<>();
        // Assumes existence of a ChatAdapter class
        chatAdapter = new ChatAdapter(chatList, this); // Assuming ChatAdapter is implemented to handle your 'chat' model class
        recyclerViewChats.setAdapter(chatAdapter);

        // Method to create chat documents for all matched users
        createChatsForAllMatches();

        // Setup BottomNavigationView and its item selection listener
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation); // Sets the chats menu item as selected
        bottomNav.setSelectedItemId(R.id.navigation_chats);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Get the item ID
                int id = item.getItemId();

                // Use if-else blocks to determine which item was selected
                if (id == R.id.navigation_profile) {
                    startActivity(new Intent(AllChatsActivity.this, ProfileSetup.class));
                    return true;
                } else if (id == R.id.navigation_main) {
                    startActivity(new Intent(AllChatsActivity.this, MainPage.class));
                    return true;
                } else if (id == R.id.navigation_chats) {
                    startActivity(new Intent(AllChatsActivity.this, AllChatsActivity.class));
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * Creates chat documents for all matches found in the Firestore "Matches" collection.
     */
    private void createChatsForAllMatches() {
        // Fetch all matches from Firestore
        db.collection("Matches")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<DocumentSnapshot> matches = task.getResult().getDocuments();
                        matchCount.set(matches.size());
                        if (matches.size() == 0) {
                            // No matches to process, fetch chats immediately
                            fetchDynamicChats(); // No matches, fetch chats directly
                        }
                        for (DocumentSnapshot match : matches) {
                            String user1Email = match.getString("user1Mail");
                            String user2Email = match.getString("user2Mail");
                            if (user1Email != null && user2Email != null) {
                                checkAndCreateChat(db, user1Email, user2Email);
                            } else {
                                processMatchCompletion(); // Decrement the count if match can't be processed
                            }
                        }
                    } else {
                        Log.w(TAG, "Error getting matches: ", task.getException());
                    }
                });

        // Initialize and enable orientation event listener
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

    /**
     * Checks if a chat document already exists for a given match, and creates one if it does not.
     *
     * @param db Firestore database instance.
     * @param user1Email The email address of the first user in the match.
     * @param user2Email The email address of the second user in the match.
     */
    private void checkAndCreateChat(FirebaseFirestore db, String user1Email, String user2Email) {
        db.collection("chats")
                .whereArrayContains("participantEmails", user1Email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        QuerySnapshot chatsSnapshot = task.getResult();
                        boolean chatExists = false;
                        for (DocumentSnapshot chatSnapshot : chatsSnapshot.getDocuments()) {
                            List<String> participantEmails = (List<String>) chatSnapshot.get("participantEmails");
                            if (participantEmails.contains(user2Email)) {
                                chatExists = true;
                                break;
                            }
                        }
                        if (!chatExists) {
                            createChatDocument(db, user1Email, user2Email);
                        } else {
                            processMatchCompletion(); // The chat already exists, no need to create a new one
                        }
                    } else {
                        Log.e(TAG, "Failed to check existing chats", task.getException());
                        processMatchCompletion(); // Even if the check fails, process the completion
                    }
                });
    }

    /**
     * Creates a new chat document in Firestore for a given match.
     *
     * @param db Firestore database instance.
     * @param user1Email The email address of the first user in the match.
     * @param user2Email The email address of the second user in the match.
     */
    private void createChatDocument(FirebaseFirestore db, String user1Email, String user2Email) {
        String uniqueChatID = db.collection("chats").document().getId();
        Map<String, Object> chatData = new HashMap<>();
        chatData.put("participantEmails", Arrays.asList(user1Email, user2Email));

        db.collection("chats").document(uniqueChatID)
                .set(chatData)
                .addOnSuccessListener(unused -> {
                    Log.d(TAG, "Chat document created with ID: " + uniqueChatID);
                    processMatchCompletion(); // Chat created, decrement the match count
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error creating chat document", e);
                    processMatchCompletion(); // Even if creation fails, process the completion
                });
    }

    /**
     * Handles completion of a match processing step, either by creating a chat document or after failing to do so.
     */
    private void processMatchCompletion() {
        if (processedMatches.incrementAndGet() == matchCount.get()) {
            // All matches have been processed, fetch chats
            fetchDynamicChats(); // Fetch chats if all matches processed
        }
    }

    /**
     * Fetches chats dynamically for the current user and updates the UI accordingly.
     */
    public void fetchDynamicChats() {
        chatList.clear();
        db.collection("chats")
                .whereArrayContains("participantEmails", currentUserId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String userName = document.getId();
                            ArrayList<String> participantEmails = (ArrayList<String>) document.get("participantEmails");
                            String lastMessage = "Last message here";
                            String timestamp = "timestamp here";
                            if (participantEmails.contains(currentUserId)) {
                                chatList.add(new chat(userName, R.drawable.ic_profile_placeholder, "", "", ""));
                            }
                        }
                        chatAdapter.notifyDataSetChanged();
                        updatePlaceholderVisibility();
                    } else {
                        Log.w(TAG, "Error fetching dynamic chats.", task.getException());
                    }
                });
    }

    /**
     * Updates the visibility of the placeholder text based on the chat list's size.
     */
    private void updatePlaceholderVisibility() {
        if (chatList.isEmpty()) {
            placeholderTextView.setVisibility(View.VISIBLE);
        } else {
            placeholderTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onChatClicked(chat chat) {
        Log.w(TAG, chat.getChatID());
        Intent intent = new Intent(AllChatsActivity.this, SimpleChat.class);
        intent.putExtra("CHAT_ID", chat.getUserName());
        intent.putExtra("RECEIVER_ID", chat.getReceiverEmail());

        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Disable the OrientationEventListener to prevent memory leaks
        orientationEventListener.disable(); // Prevent memory leaks by disabling the listener
    }

}