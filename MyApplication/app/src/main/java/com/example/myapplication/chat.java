package com.example.myapplication;

public class chat {
    private String userName;
    private String lastMessage;
    private String timestamp;
    private String recId;

    private String chatID;
    private int userProfileImageId; // Drawable resource ID for the user image

    public chat(String userName, String lastMessage, String timestamp, int userProfileImageId, String recId, String chatID) {
        this.userName = userName;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
        this.userProfileImageId = userProfileImageId;
        this.recId = recId;
        this.chatID = chatID;
    }

    public String getChatID() {
        return chatID;
    }

    public String getRecId() {
        return recId;
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
}
