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

    public chat() {
    }

    // Constructor updated to include sender and receiver email
    public chat(String userName,  int userProfileImageId, String chatID, String senderEmail, String receiverEmail) {
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

    //setter method for email
    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    //getter method for chatid
    public String getChatID() {
        return chatID;
    }

    // Getters and setters
    public String getUserName() {
        return userName;
    }

    //getter method for last message
    public String getLastMessage() {
        return lastMessage;
    }

    //getter method for timestamp
    public String getTimestamp() {
        return timestamp;
    }

    //setter method for user image profile
    public int getUserProfileImageId() {
        return userProfileImageId;
    }

    //setter method for on click
    public void setOnClickListener(View.OnClickListener onClickListener) {
    }

}
