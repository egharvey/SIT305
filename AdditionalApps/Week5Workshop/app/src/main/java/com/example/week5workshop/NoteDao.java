package com.example.week5workshop;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert
    void insert(Note note);

    @Query("SELECT * FROM notes ORDER BY id DESC")
    List<Note> getAllNotes();

    @Query("DELETE FROM notes")
    void deleteAllNotes();
}
