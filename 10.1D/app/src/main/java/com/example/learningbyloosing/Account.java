package com.example.learningbyloosing;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

//DO NOT STORE ACCOUNTS IN A LOCAL DATABASE
@Entity(tableName = "accounts")
public class Account {

    @PrimaryKey (autoGenerate = true)
    private int id;

    private String username, email, password, topics;
    private int phone;
    private boolean isPremium;

    public Account(String username, String email, String password, String topics, int phone, boolean premium){
        this.username = username;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.topics = topics;
        this.isPremium = premium;
    }

    //Getters
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getTopics() { return topics; }
    public int getPhone() { return phone; }
    public boolean getIsPremium() {return isPremium; }

    //Setters (room needs set id
    public void setId(int id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setTopics(String topics) { this.topics = topics; }
    public void setPhone(int phone) { this.phone = phone; }
    public void setIsPremium(boolean premium) { this.isPremium = premium; }
}
