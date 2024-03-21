package com.example.myapplication;

public class chat {
    private String userName;
    private String lastMessage;
    private String timestamp;
    private int userProfileImageId; // Drawable resource ID for the user image

    public chat(String userName, String lastMessage, String timestamp, int userProfileImageId) {
        this.userName = userName;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
        this.userProfileImageId = userProfileImageId;
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
