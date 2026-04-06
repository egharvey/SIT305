package com.example.personalplanner;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

@Entity(tableName = "events")
public class Event {

    @PrimaryKey (autoGenerate = true)
    private int id;

    private String title, location, category;
    private Long time;

    public Event(String title, String location, String category, Long time) {
        this.title = title;
        this.location = location;
        this.category = category;
        this.time = time;
    }

    //Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getLocation() { return location; }
    public String getCategory() { return category; }
    public Long getTime() { return time; }

    //Setters (room needs set id
    public void setId(int id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setLocation(String location) { this.location = location; }
    public void setCategory(String category) { this.category = category; }
    public void setTime(Long time) { this.time = time; }

}
