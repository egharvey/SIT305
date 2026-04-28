package com.example.talktoawall;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    //Constants
    private static final String DB_NAME = "wall.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_MESSAGES = "messages";

    private static final String COL_MESSAGES_ID = "id";
    private static final String COL_MESSAGES_MESSAGE = "message";
    private static final String COL_MESSAGES_TIMESTAMP = "timestamp";
    private static final String COL_MESSAGES_USERNAME = "username";
    private static final String COL_MESSAGES_ISBOT = "is_bot";

    //SET UP
    public DatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE " + TABLE_MESSAGES + " (" +
                COL_MESSAGES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_MESSAGES_MESSAGE + " TEXT NOT NULL, " +
                COL_MESSAGES_TIMESTAMP + " TEXT NOT NULL, " +
                COL_MESSAGES_USERNAME + " TEXT NOT NULL, " +
                COL_MESSAGES_ISBOT + " BOOLEAN NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
        onCreate(db);
    }
    
    //TABLE METHODS
    public long insertMessage(Message message) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_MESSAGES_MESSAGE, message.getMessage());
        cv.put(COL_MESSAGES_TIMESTAMP, message.getTimestamp());
        cv.put(COL_MESSAGES_USERNAME, message.getUsername());
        cv.put(COL_MESSAGES_ISBOT, message.getIsBot());
        return db.insert(TABLE_MESSAGES, null, cv);
    }

    public void deletePost(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MESSAGES, COL_MESSAGES_ID + "=?", new String[]{String.valueOf(id)});
    }
    
    public List<Message> getUserMessageHistory(String username){
        List<Message> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from "+ TABLE_MESSAGES + " where " + COL_MESSAGES_USERNAME + " = " + username, null);
        if(cursor.moveToFirst()){
            do {
                list.add(cursorToEntry(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    private Message cursorToEntry(Cursor cursor){
        boolean isBot = false;
        if (cursor.getInt(cursor.getColumnIndexOrThrow(COL_MESSAGES_ISBOT)) == 1) isBot = true;
        return new Message(
                cursor.getInt(cursor.getColumnIndexOrThrow(COL_MESSAGES_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_MESSAGES_MESSAGE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_MESSAGES_TIMESTAMP)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_MESSAGES_USERNAME)),
                isBot
        );
    }
}
