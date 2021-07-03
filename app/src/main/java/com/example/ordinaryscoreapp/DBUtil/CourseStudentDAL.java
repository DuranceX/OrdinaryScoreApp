package com.example.ordinaryscoreapp.DBUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.ordinaryscoreapp.Model.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Xie Jiadi
 * @time 2021/7/1 8:25
 * @description 课程学生表数据操作类
 */
public class CourseStudentDAL{
    private String TABLE_NAME = "course_student";
    private Cursor cursor;
    private DBOpenHelper dbOpenHelper;
    private SQLiteDatabase db;

    public CourseStudentDAL(Context context){
        dbOpenHelper = new DBOpenHelper(context,Constants.DatabaseName,null, Constants.DatabaseVersion);
        db = dbOpenHelper.getWritableDatabase();
    }

    /**
     * @author Xie Jiadi
     * @time 2021/7/1 9:02
     * @description 课程学生数据添加方法
     * @param id 课程编号
     * @param no 学生学号
     * @param score 课程成绩
     */
    public long dbAdd(String id,String no,@Nullable Float score){
        ContentValues values = new ContentValues();
        values.put("course_id",id);
        values.put("student_no",no);
        values.put("score",score);
        long result = db.insert(TABLE_NAME,null,values);
        if(result == -1){
            Log.i("Database","addFailed");
        }
        else
            Log.i("Database","addSucceed");
        return result;
    }

    /**
     * @author Xie Jiadi
     * @time 2021/7/1 9:05
     * @description 课程学生数据删除方法
     * @param no 学生学号
     * @param id 课程编号
     */
    public int dbDel(@Nullable String id,@Nullable String no){
        String where="course_id='" + id + "'";;
        if(!no.equals(""))
            where = where + " and student_no='" + no + "'";

        int result = db.delete(TABLE_NAME,where,null);
        if(result > 0){
            Log.i("DataBase","delSucceed");
        }
        else
            Log.i("DataBase","delFailed");
        return result;
    }


    /**
     * @author Xie Jiadi
     * @time 2021/7/1 9:06
     * @description 课程学生数据更新方法
     * @param no 学生学号
     * @param id 课程编号
     * @param score 学生成绩
     */
    public long dbUpdate(String id,String no,Float score){
        ContentValues values = new ContentValues();
        values.put("score",score);
        String where = "student_no='" + no + "' and course_id='" + id + "'";
        long result = db.update(TABLE_NAME,values,where,null);
        if(result > 0){
            Log.i("DataBase","updateSucceed");
        }
        else
            Log.i("DataBase","updateFailed");
        return result;
    }


    /**
     * @author Xie Jiadi
     * @time 2021/7/1 9:08
     * @description 查询所有课程学生数据
     */
    public Map[] dbFindAll(){
        cursor = db.query(TABLE_NAME, null, null, null, null, null, "course_id ASC");
        cursor.moveToFirst();
        Map item[] = new Map[cursor.getCount()];
        for(int i=0;i<cursor.getCount();i++)
            item[i] = new HashMap<String, Object>();
        int i=0;
        while(!cursor.isAfterLast()){
            String id = cursor.getString(0);
            String no = cursor.getString(1);
            String score = cursor.getString(2);
            item[i].put("course_id",id);
            item[i].put("student_no",no);
            item[i++].put("score",score);
            cursor.moveToNext();
        }
        return item;
    }



    /**
     * @author Xie Jiadi
     * @time 2021/7/1 9:09
     * @description 查询满足条件的课程学生数据
     * @param where 查询条件语句
     */
    public Map[] dbFind(String where){
        cursor = db.query(TABLE_NAME, null, where, null, null, null, "course_id ASC");
        cursor.moveToFirst();
        Map item[] = new Map[cursor.getCount()];
        if(cursor.getCount() == 0){
            Log.i("DataBase","没有查询到数据");
        }
        for(int i=0;i<cursor.getCount();i++)
            item[i] = new HashMap<String, Object>();
        int i=0;
        while(!cursor.isAfterLast()){
            String id = cursor.getString(1);
            String no = cursor.getString(2);
            String score = cursor.getString(3);
            item[i].put("course_id",id);
            item[i].put("student_no",no);
            item[i++].put("score",score);
            cursor.moveToNext();
        }
        return item;
    }
}
