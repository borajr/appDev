package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class AllChatsActivity extends AppCompatActivity implements ChatAdapter.OnChatClickListener {
    private RecyclerView recyclerView;
    private ChatAdapter adapter;
    private List<chat> chatList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_chats);

        chatList = new ArrayList<>();
        chatList.add(new chat("User1", "Hello", "12:00", R.drawable.ic_profile_placeholder));
        chatList.add(new chat("User2", "How are you?", "12:05", R.drawable.ic_profile_placeholder));
        // Add more chats as needed

        recyclerView = findViewById(R.id.recyclerView_chats);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatAdapter(chatList, this);
        recyclerView.setAdapter(adapter);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle bottom navigation item clicks here
                return true;
            }
        });
    }

    @Override
    public void onChatClicked(chat chat) {
        Intent intent = new Intent(AllChatsActivity.this, SimpleChat.class);
        // Optionally pass data to SimpleChat, e.g., the username of the clicked chat
        intent.putExtra("userName", chat.getUserName());
        startActivity(intent);
    }
}
