package com.example.myapplication;

public class User {
    private String userEmail;
    private String name;
    private String profileImageUrl;
    public User(String userId, String name, String profileImageUrl){
        this.userEmail = userId;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
    }

    public String getUserId(){
        return userEmail;
    }
    public void setUserID(String userID){
        this.userEmail = userEmail;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getProfileImageUrl(){
        return profileImageUrl;
    }
    public void setProfileImageUrl(String profileImageUrl){
        this.profileImageUrl = profileImageUrl;
    }
}