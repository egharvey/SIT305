package com.example.week5workshop;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes")
public class Note {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String title;

    public Note(){

    }

    @Ignore
    public Note(String title){ this.title = title; }
}
