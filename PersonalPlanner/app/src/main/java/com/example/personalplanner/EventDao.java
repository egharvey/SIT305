package com.example.personalplanner;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface EventDao {

    @Insert
    void insert(Event event);

    @Update
    void update(Event event);

    @Query("SELECT * FROM events ORDER BY time ASC")
    List<Event> getAllEvents();

    @Query("DELETE FROM events WHERE id = :id")
    void deleteEvent(int id);
}

