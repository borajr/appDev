package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private List<chat> chatList;
    private OnChatClickListener listener;

    public ChatAdapter(List<chat> chatList, OnChatClickListener listener) {
        this.chatList = chatList;
        this.listener = listener;
    }

    public interface OnChatClickListener {
        void onChatClicked(chat chat);
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
        Glide.with(holder.itemView.getContext())
                .load(chat.getUserProfileImageId()) // Use the correct getter method
                .placeholder(R.drawable.ic_profile_placeholder) // Placeholder image
                .into(holder.userImageView);

        holder.itemView.setOnClickListener(v -> listener.onChatClicked(chat));
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView userImageView;
        TextView userNameView, lastMessageView, timestampView;

        public ViewHolder(View itemView) {
            super(itemView);
            userImageView = itemView.findViewById(R.id.chat_item_image);
            userNameView = itemView.findViewById(R.id.chat_item_name);
        }
    }
}
