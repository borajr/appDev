package com.example.myapplication;

public class ChatMessage {
    private String message;
    private boolean isSent; // 'true' if this message was sent by the user, 'false' if received
    private String timestamp; // Optional: For message time
    private int userProfileImageId; // Optional: For the image resource ID (if message is received)

    public ChatMessage(String message, boolean isSent, String timestamp, int userProfileImageId) {
        this.message = message;
        this.isSent = isSent;
        this.timestamp = timestamp;
        this.userProfileImageId = userProfileImageId;
    }

    // Getters
    public String getMessage() {
        return message;
    }

    public boolean isSent() {
        return isSent;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getUserProfileImageId() {
        return userProfileImageId;
    }

    // Setters
    public void setMessage(String message) {
        this.message = message;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setUserProfileImageId(int userProfileImageId) {
        this.userProfileImageId = userProfileImageId;
    }
}
