package com.example.istream;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

//DO NOT STORE ACCOUNTS IN A LOCAL DATABASE
@Entity(tableName = "accounts")
public class Account {

    @PrimaryKey (autoGenerate = true)
    private int id;

    private String fullName, username, password, bookmarks;

    public Account(String fullName, String username, String password, String bookmarks){
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.bookmarks = bookmarks;
    }

    //Getters
    public int getId() { return id; }
    public String getFullName() { return fullName; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getBookmarks() { return bookmarks; }

    //Setters (room needs set id
    public void setId(int id) { this.id = id; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setBookmarks(String bookmarks) { this.bookmarks = bookmarks; }
}
