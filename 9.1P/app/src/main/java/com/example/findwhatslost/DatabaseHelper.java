package com.example.findwhatslost;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    //USEFUL CONSTANTS ----------------------------------------------
    private static final String DB_NAME = "lost.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_POSTS = "posts";

    private static final String COL_POSTS_ID = "id";
    private static final String COL_POSTS_NAME = "name";
    private static final String COL_POSTS_PHONE = "phone";
    private static final String COL_POSTS_DESCRIPTION = "description";
    private static final String COL_POSTS_LOCATION = "location";
    private static final String COL_POSTS_IMAGE = "image";
    private static final String COL_POSTS_FOUND_DATE= "found_date";
    private static final String COL_POSTS_POSTED_DATE = "posted_date";
    private static final String COL_POSTS_POSTED_LAT = "posted_latitude";
    private static final String COL_POSTS_POSTED_LONG = "posted_longitude";

    //DATABASE SET UP ------------------------------------------------
    public DatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE " + TABLE_POSTS + " (" +
                COL_POSTS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_POSTS_NAME + " TEXT NOT NULL, " +
                COL_POSTS_PHONE + " INTEGER NOT NULL, " +
                COL_POSTS_DESCRIPTION + " TEXT NOT NULL, " +
                COL_POSTS_LOCATION + " TEXT NOT NULL, " +
                COL_POSTS_IMAGE + " TEXT NOT NULL, " +
                COL_POSTS_FOUND_DATE + " TEXT NOT NULL, " +
                COL_POSTS_POSTED_DATE + " TEXT NOT NULL, "+
                COL_POSTS_POSTED_LAT + " DOUBLE, " +
                COL_POSTS_POSTED_LONG + " DOUBLE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POSTS);
        onCreate(db);
    }

    //TABLE METHODS ----------------------------------------------------
    public long insertPost(Post post) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_POSTS_NAME, post.getName());
        cv.put(COL_POSTS_PHONE, post.getPhone());
        cv.put(COL_POSTS_DESCRIPTION, post.getDescription());
        cv.put(COL_POSTS_LOCATION, post.getLocation());
        cv.put(COL_POSTS_IMAGE, post.getImage());
        cv.put(COL_POSTS_FOUND_DATE, post.getFoundDate());
        cv.put(COL_POSTS_POSTED_DATE, post.getPostedDate());
        cv.put(COL_POSTS_POSTED_LAT, post.getLatitude());
        cv.put(COL_POSTS_POSTED_LONG, post.getLongitude());
        return db.insert(TABLE_POSTS, null, cv);
    }

    public int updatePost(Post post) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_POSTS_NAME, post.getName());
        cv.put(COL_POSTS_PHONE, post.getPhone());
        cv.put(COL_POSTS_DESCRIPTION, post.getDescription());
        cv.put(COL_POSTS_LOCATION, post.getLocation());
        cv.put(COL_POSTS_IMAGE, post.getImage());
        cv.put(COL_POSTS_FOUND_DATE, post.getFoundDate());
        cv.put(COL_POSTS_POSTED_DATE, post.getPostedDate());
        return db.update(TABLE_POSTS, cv, COL_POSTS_ID + "+?", new String[]{String.valueOf(post.getId())});
    }

    public void deletePost(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_POSTS, COL_POSTS_ID + "=?", new String[]{String.valueOf(id)});
    }

    public List<Post> getAllPosts(){
        List<Post> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_POSTS, null);
        if(cursor.moveToFirst()){
            do {
                list.add(cursorToEntry(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public List<Post> searchPosts(String query){
        List<Post> list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String search = "%" + query + "%";
        Cursor cursor = db.query(TABLE_POSTS, null,
                COL_POSTS_NAME + " LIKE ? OR " +
                        COL_POSTS_DESCRIPTION + " LIKE ? OR " +
                        COL_POSTS_LOCATION  + " LIKE ? OR " +
                        COL_POSTS_POSTED_DATE  + " LIKE ? OR " +
                        COL_POSTS_FOUND_DATE  + " LIKE ?",
                new String[]{search, search, search, search, search}, null, null, COL_POSTS_POSTED_DATE + " DESC");
        if(cursor.moveToFirst()){
            do {
                list.add(cursorToEntry(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    private Post cursorToEntry(Cursor cursor){
        return new Post(
                cursor.getInt(cursor.getColumnIndexOrThrow(COL_POSTS_ID)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COL_POSTS_PHONE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_POSTS_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_POSTS_DESCRIPTION)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_POSTS_LOCATION)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_POSTS_IMAGE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_POSTS_FOUND_DATE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_POSTS_POSTED_DATE)),
                cursor.getDouble(cursor.getColumnIndexOrThrow(COL_POSTS_POSTED_LAT)),
                cursor.getDouble(cursor.getColumnIndexOrThrow(COL_POSTS_POSTED_LONG))
        );
    }
}
