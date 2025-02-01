package com.example.bookapp;

public class User {

    private String userId;
    private String name;
    private String email;
    private String role;
    private String photoUrl; // Changed PhotoUrl to photoUrl (consistent with naming conventions)

    // Default constructor (required for Firebase)
    public User() {
    }

    // Constructor
    public User(String name, String email, String role) {
        this.name = name;
        this.email = email;
        this.role = role;
    }

    // Getter and Setter for userId
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    // Getter and Setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and Setter for email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter and Setter for role
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // Getter and Setter for photoUrl
    public String getPhotoUrl() {
        return photoUrl; // Return the correct field
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl; // Set the correct field
    }
}
