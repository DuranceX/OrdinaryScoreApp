package com.example.ordinaryscoreapp.DBUtil;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class StudentDAL{
    private String TABLE_NAME = "student";
    private Cursor cursor;
    private DBOpenHelper dbOpenHelper;
    private SQLiteDatabase db;

    public StudentDAL(Context context){
        dbOpenHelper = new DBOpenHelper(context,TABLE_NAME,null,1);
        db = dbOpenHelper.getWritableDatabase();
    }
}
