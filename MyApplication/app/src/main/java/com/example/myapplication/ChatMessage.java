package com.example.myapplication;

public class ChatMessage {
    private String messageText;

    public boolean isSent;
    private String messageUser;
    private String messageTime;
    public ChatMessage(String messageText, String messageUser){
        this.messageText = messageText;
        this.messageUser = messageUser;

        // Initialize to current time
    }
    public ChatMessage(){
    }



    public String getMessageText() {
        return messageText;
    }
    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
    public String getMessageUser() {
        return messageUser;
    }
    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public boolean isSent() {
        return true;
    }


}