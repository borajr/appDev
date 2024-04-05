package com.example.myapplication;

import android.view.View;

public class chat {
    private String userName;
    private String lastMessage;
    private String timestamp;
    private int userProfileImageId; // Drawable resource ID for the user image
    private String chatID; // Chat ID
    private String senderEmail; // Sender's email address
    private String receiverEmail; // Receiver's email address

    // Constructor updated to include sender and receiver email
    public chat(String userName, int userProfileImageId, String chatID, String senderEmail, String receiverEmail) {
        this.userName = userName;
        this.userProfileImageId = userProfileImageId;
        this.chatID = chatID;
        this.senderEmail = senderEmail;
        this.receiverEmail = receiverEmail;
    }

    // Getters and setters for new fields
    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public String getChatID() {
        return chatID;
    }


    // Getters and setters
    public String getUserName() {
        return userName;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getUserProfileImageId() {
        return userProfileImageId;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
    }

}
