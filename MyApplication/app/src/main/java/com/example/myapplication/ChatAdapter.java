package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private List<chat> chatList;

    public ChatAdapter(List<chat> chatList) {
        this.chatList = chatList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        chat chat = chatList.get(position);
        holder.userNameView.setText(chat.getUserName());
        holder.lastMessageView.setText(chat.getLastMessage());
        holder.timestampView.setText(chat.getTimestamp());
        holder.userImageView.setImageResource(chat.getUserProfileImageId());
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView userImageView;
        public TextView userNameView, lastMessageView, timestampView;

        public ViewHolder(View itemView) {
            super(itemView);
            userImageView = itemView.findViewById(R.id.chat_item_image);
            userNameView = itemView.findViewById(R.id.chat_item_name);
            lastMessageView = itemView.findViewById(R.id.chat_item_message_preview);
            timestampView = itemView.findViewById(R.id.chat_item_timestamp);
        }
    }
}
