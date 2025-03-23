package com.osmosis.backend.entity;


import lombok.Data;


public class Account {
    int id;
    String email;
    String username;
    String password;
    String role;

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    
    public String getRole() {
        return role;
    }
}
