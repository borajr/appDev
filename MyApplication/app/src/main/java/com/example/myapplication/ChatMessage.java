package com.example.myapplication;

public class ChatMessage {
    private String message;
    private boolean isSent;
    private String timestamp;
    private int userProfileImageId;

    // No-argument constructor
    public ChatMessage() {
        // Default constructor required for Firestore data model deserialization
    }

    // All-argument constructor
    public ChatMessage(String message, boolean isSent, String timestamp, int userProfileImageId) {
        this.message = message;
        this.isSent = isSent;
        this.timestamp = timestamp;
        this.userProfileImageId = userProfileImageId;
    }

    // Getters and setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getUserProfileImageId() {
        return userProfileImageId;
    }

    public void setUserProfileImageId(int userProfileImageId) {
        this.userProfileImageId = userProfileImageId;
    }
}
