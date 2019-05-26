package com.example.niptictfeedback.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.niptictfeedback.models.User;

import java.util.ArrayList;

public class UserDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "feedback";
    private static final String TABLE_NAME = "users";
    private static final String STUDENT_ID = "student_id";
    private static final String PASSWORD = "password";
    private static final String USER_NAME = "name";
    private static final String USER_ROLE_ID = "user_role_id";
    private static final String PROFILE_PICTURE = "profile_img";

    public UserDBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+TABLE_NAME+"(id integer primary key,name text,student_id text,password text,user_role_id text,profile_img text )");
        db.close();
    }

    public void insertUser(String name,String password,String student_id,String user_role_id,String profile_img){

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id",1);
        values.put(USER_NAME,name);
        values.put(STUDENT_ID,student_id);
        values.put(PASSWORD,password);
        values.put(USER_ROLE_ID,user_role_id);
        values.put(PROFILE_PICTURE,profile_img);

        db.insert(TABLE_NAME,null,values);

        db.close();

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        db.close();
    }

    public User getLoginUser(){
        User user = null;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE id=1",null);

        if (cursor.getCount()>0){
            cursor.moveToFirst();
            user = new User(cursor.getString(2),cursor.getString(1),cursor.getString(3),cursor.getString(4),cursor.getString(5));
        }
        db.close();
        return user;

    }

    public void dropTable(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        db.close();
    }

    public void createNewTable(){
        SQLiteDatabase db = getWritableDatabase();
        if(checkForTableExists(db,TABLE_NAME)){
            return;
        }
        onCreate(db);
    }

    private boolean checkForTableExists(SQLiteDatabase db, String table){
        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='"+table+"'";
        Cursor mCursor = db.rawQuery(sql, null);
        if (mCursor.getCount() > 0) {
            return true;
        }
        mCursor.close();
        return false;
    }

}
