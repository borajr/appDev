package com.example.myapplication;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class User {
    private String userEmail;
    FirebaseStorage storage;
    private String name;
    private String profileImageUrl;
    private String department;
    private String food;
    private boolean alcohol;
    private boolean smoking;
    private boolean weed;
    private int age;
    private int height; // Assuming height is stored as an integer

    private String gender;

    // Constructor
    public User(String userEmail, String name, String profileImageUrl,
                String department, String food, boolean alcohol, boolean smoking,
                boolean weed, int age, int height, String gender) {
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
        this.gender = gender;
    }
    // Getters and setters for all properties
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }

    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }

    public int getAge() {return age;}

    public String getImageStoragePath() {
        // Assuming the image is named image0 and the email is used as the directory name
        return getEmail().replace('.', '_') + "/image0"; // Replacing '.' with '_' to avoid Firebase Storage issues with email
    }

    public String getEmail(){return userEmail;}

    public void setAge(int age) {this.age = age;}

    public String getProfileImageUrl() {
        if (this.userEmail != null) {
            // Assuming the profile image name is fixed, e.g., "image0"
            // Adjust the path if necessary
            return ("gs://dbl-app-development-ccc78.appspot.com/b" + this.userEmail.replace("@", "%40") + "/image0");
        } else {
            // Handle the case where userEmail is null
            // For example, return a default path or log an error
            return "gs://dbl-app-development-ccc78.appspot.com/boran@student.tue.nl/image0"; // Just an example, adjust based on your needs
        }
    }

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

    public String getGender(){return gender;}
    public void setGender(String gender) { this.gender = gender; }
}
