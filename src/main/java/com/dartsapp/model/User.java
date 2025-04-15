package com.dartsapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users") // maps to the "users" table
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id") // adjust based on your table definition (could be "id" or "userID")
    private Integer id;
    
    @Column(name = "username", nullable = false, unique = true)
    private String username;
    
    @Column(name = "password", nullable = false)
    private String password;
    
    @Column(name = "main_status", nullable = false)
    private String mainStatus; // e.g., "offline", "online"
    
    @Column(name = "sub_status")
    private String subStatus; // e.g., "available", "away", "in_game"

    // Default constructor
    public User() {
    }

    // Constructor with fields
    public User(String username, String password, String mainStatus, String subStatus) {
        this.username = username;
        this.password = password;
        this.mainStatus = mainStatus;
        this.subStatus = subStatus;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getMainStatus() {
        return mainStatus;
    }
    
    public void setMainStatus(String mainStatus) {
        this.mainStatus = mainStatus;
    }
    
    public String getSubStatus() {
        return subStatus;
    }
    
    public void setSubStatus(String subStatus) {
        this.subStatus = subStatus;
    }
}
