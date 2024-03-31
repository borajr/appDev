package com.example.myapplication;

public class User {
    private String userEmail;
    private String name;
    private String profileImageUrl;
    private String department;
    private String food;
    private boolean alcohol;
    private boolean smoking;
    private boolean weed;
    private int age;
    private int height; // Assuming height is stored as an integer

    // Constructor
    public User(String userEmail, String name, String profileImageUrl,
                String department, String food, boolean alcohol, boolean smoking,
                boolean weed, int age, int height) {
        this.userEmail = userEmail;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.department = department;
        this.food = food;
        this.alcohol = alcohol;
        this.smoking = smoking;
        this.weed = weed;
        this.age = age;
        this.height = height;
    }
    // Getters and setters for all properties
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }

    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }

    //public String getStarSign() { return starSign; }
    //public void setStarSign(String starSign) { this.starSign = starSign; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    //public String getLanguage() { return language; }
    //public void setLanguage(String language) { this.language = language; }

    public boolean getAlcohol() { return alcohol; }
    public void setAlcohol(boolean alcohol) { this.alcohol = alcohol; }

    public boolean getSmoking() { return smoking; }
    public void setSmoking(boolean smoking) { this.smoking = smoking; }

    public boolean getWeed() { return weed; }
    public void setWeed(boolean weed) { this.weed = weed; }

    public String getPreferredDiet() { return food; }
    public void setPreferredDiet(String preferredDiet) { this.food = preferredDiet; }
}
