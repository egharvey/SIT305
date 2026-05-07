package com.example.learningbyloosing;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface HistoryDao {

    @Insert
    void insert(History history);

    @Update
    void update(History history);

    @Query("SELECT * FROM history WHERE userID = :userID")
    List<History> getUserHistory(String userID);
}
