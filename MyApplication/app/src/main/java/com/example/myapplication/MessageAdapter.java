package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;

/**
 * Adapter for displaying messages in a RecyclerView.
 */
class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<String> messages; // List of message strings

    /**
     * Constructor for MessageAdapter.
     * @param messages List of messages to be displayed
     */
    MessageAdapter(List<String> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each message item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sent_message_item, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        // Bind the message to the ViewHolder
        holder.bind(messages.get(position));
    }

    @Override
    public int getItemCount() {
        // Return the total number of messages
        return messages.size();
    }

    /**
     * ViewHolder for message items.
     */
    class MessageViewHolder extends RecyclerView.ViewHolder {

        TextView messageText; // TextView for displaying the message body

        MessageViewHolder(View itemView) {
            super(itemView);
            // Find the TextView by its ID
            messageText = itemView.findViewById(R.id.text_message_body_sent);
        }

        /**
         * Bind the message string to the TextView.
         * @param message Message string to be displayed
         */
        void bind(String message) {
            messageText.setText(message);
        }
    }
}