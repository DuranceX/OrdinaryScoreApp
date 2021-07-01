package com.example.ordinaryscoreapp.DBUtil;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBOpenHelper extends SQLiteOpenHelper {

    final String CREATE_STUDENT_TABLE_SQL = "create table student(" +
            "student_no varchar primary key," +
            "student_name varchar," +
            "student_class varchar)";
    final String CREATE_COURSE_TABLE_SQL = "create table course(course_id varchar primary key," +
            "course_title varchar," +
            "course_location varchar," +
            "course_time varchar)";
    final String CREATE_COURSE_STUDENT_TABLE_SQL = "create table course_student(_id integer primary key autoincrement," +
            "course_id varchar," +
            "student_no varchar," +
            "score float," +
            "foreign key(course_id) references course(course_id)," +
            "foreign key(student_no) references student(student_no))";
    final String CREATE_COURSE_SCORE = "create table course_score(_id integer primary key autoincrement," +
            "course_id varchar," +
            "student_no varchar," +
            "checkin_no_1 float," +
            "checkin_no_2 float," +
            "checkin_no_3 float," +
            "checkin_no_4 float," +
            "checkin_no_5 float," +
            "homework_no_1 float," +
            "homework_no_2 float," +
            "homework_no_3 float," +
            "homework_no_4 float," +
            "homework_no_5 float," +
            "program_no_1 float," +
            "program_no_2 float," +
            "program_no_3 float," +
            "program_no_4 float," +
            "program_no_5 float," +
            "totalScore float," +
            "foreign key(course_id) references course(course_id)," +
            "foreign key(student_no) references student(student_no))";

    public DBOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys = ON");
        db.execSQL(CREATE_STUDENT_TABLE_SQL);
        db.execSQL(CREATE_COURSE_TABLE_SQL);
        db.execSQL(CREATE_COURSE_STUDENT_TABLE_SQL);
        db.execSQL(CREATE_COURSE_SCORE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("PRAGMA foreign_keys = ON");
        String SQL = "DROP TABLE course_student";
        db.execSQL(SQL);
        SQL = "DROP TABLE course_score";
        db.execSQL(SQL);
        SQL = "DROP TABLE student";
        db.execSQL(SQL);

        db.execSQL(CREATE_STUDENT_TABLE_SQL);
        db.execSQL(CREATE_COURSE_STUDENT_TABLE_SQL);
        db.execSQL(CREATE_COURSE_SCORE);
        Log.i("DataBase","更新数据表列");
    }
}
