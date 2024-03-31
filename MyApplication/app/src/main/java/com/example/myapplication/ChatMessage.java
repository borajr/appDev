package com.example.myapplication;

public class ChatMessage {
    private String message;
    private boolean isSent;
    private String timestamp;
    private int userProfileImageId;
    private String senderId; // Add senderId
    private String receiverId; // Add receiverId

    // No-argument constructor for Firestore deserialization
    public ChatMessage() {}

    // Constructor with all arguments
    public ChatMessage(String message, boolean isSent, String timestamp, int userProfileImageId, String senderId, String receiverId) {
        this.message = message;
        this.isSent = isSent;
        this.timestamp = timestamp;
        this.userProfileImageId = userProfileImageId;
        this.senderId = senderId;
        this.receiverId = receiverId;
    }


// Getters and setters
    // ... Include all getters and setters, including those for senderId and receiverId

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

    public Object getSenderId() {
        return senderId;
    }
    public String getReceiverId() {
        return receiverId;
    }
}
