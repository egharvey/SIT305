package com.example.learningbyloosing;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AccountDao {
    @Insert
    void insert(Account account);

    @Update
    void update(Account account);

    @Query("UPDATE accounts SET topics = :topics WHERE username = :username")
    void updateTopics(String username, String topics);

    @Query("SELECT * FROM accounts ORDER BY id ASC")
    List<Account> getAllAccounts();

    @Query("SELECT * FROM accounts WHERE username = :username ORDER BY id ASC LIMIT 1")
    Account getAccountByUsername(String username);
}
