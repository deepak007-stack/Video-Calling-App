package com.layout.my_duo;

public class User {

    String name, email; // Do not store passwords in Firestore

    public User() {


    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Intentionally no password field or accessors for security


}
