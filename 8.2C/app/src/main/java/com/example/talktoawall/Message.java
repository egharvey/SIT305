package com.example.talktoawall;

public class Message {
    private int id;

    private String message, timestamp, username;
    private boolean isBot;

    public Message(int id, String message, String timestamp, String username, boolean isBot){
        this.id = id;
        this.message = message;
        this.timestamp = timestamp;
        this.username = username;
        this.isBot = isBot;
    }

    //Getters
    public int getId() { return id; }
    public String getMessage() { return message; }
    public String getTimestamp() { return timestamp; }
    public String getUsername() { return username; }
    public boolean getIsBot() { return isBot; }

    //Setters (room needs set id)
    public void setId(int id) { this.id = id; }
    public void setMessage(String message) { this.message = message; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    public void setUsername(String username) { this.username = username; }
    public void setIsBot(boolean isBot) { this.isBot = isBot; }
}
