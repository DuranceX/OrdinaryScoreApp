package com.example.ordinaryscoreapp.DBUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Xie Jiadi
 * @time 2021/6/29 15:45
 * @description 课程表数据操作类
 */
public class CourseDAL {
    private String TABLE_NAME = "course";
    private Cursor cursor;
    private DBOpenHelper dbOpenHelper;
    private SQLiteDatabase db;

    public CourseDAL(Context context){
        dbOpenHelper = new DBOpenHelper(context,TABLE_NAME,null,1);
        db = dbOpenHelper.getWritableDatabase();
    }

    /**
     * @author Xie Jiadi
     * @time 2021/6/29 16:01
     * @description 添加数据
     * @param title 课程名
     * @param location 课程地点
     * @param time 课程时间
     * @param id 课程编号
     */
    public void dbAdd(String id, String title, String location,String time){
        ContentValues values = new ContentValues();
        values.put("course_id",id);
        values.put("course_title",title);
        values.put("course_location",location);
        values.put("course_time",time);
        long result = db.insert(TABLE_NAME,null,values);
        if(result == -1){
            Log.i("Database","addFailed");
        }
        else
            Log.i("Database","addSucceed");
    }

    /**
     * @author Xie Jiadi
     * @time 2021/6/29 16:06
     * @description 数据删除方法
     * @param title 课程名
     * @param id 课程编号
     */
    public void dbDel(@Nullable String id, @Nullable String title){
        String where;
        if(!id.equals(""))
            where = "course_id='" + id + "'";
        else
            where = "course_title='" + title + "'";

        int result = db.delete(TABLE_NAME,where,null);
        if(result > 0){
            Log.i("DataBase","delSucceed");
        }
        else
            Log.i("DataBase","delFailed");
    }

    /**
     * @author Xie Jiadi
     * @time 2021/6/29 16:09
     * @description 数据更新方法
     * @param id 课程编号
     * @param title 课程名
     * @param location 课程地点
     * @param time 课程时间
     */
    public void dbUpdate(String id,String title,String location,String time){
        ContentValues values = new ContentValues();
        values.put("course_id",id);
        values.put("course_title",title);
        values.put("course_location",location);
        values.put("course_time",time);
        String where = "course_id=" + id;
        long result = db.update(TABLE_NAME,values,where,null);
        if(result > 0){
            Log.i("DataBase","updateSucceed");
        }
        else
            Log.i("DataBase","updateFailed");
    }

    /**
     * @author Xie Jiadi
     * @time 2021/6/29 16:17
     * @description 返回所有数据
     * @return Map[]
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
            String title = cursor.getString(1);
            String location = cursor.getString(2);
            String time = cursor.getString(3);
            item[i].put("course_id",id);
            item[i].put("course_title",title);
            item[i].put("course_location",location);
            item[i++].put("course_time",time);
            cursor.moveToNext();
        }
        return item;
    }

    /**
     * @author Xie Jiadi
     * @time 2021/6/29 16:24
     * @description 查询数据
     * @param where 判断条件
     * @return Map[]
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
            String id = cursor.getString(0);
            String title = cursor.getString(1);
            String location = cursor.getString(2);
            String time = cursor.getString(3);
            item[i].put("course_id",id);
            item[i].put("course_title",title);
            item[i].put("course_location",location);
            item[i++].put("course_time",time);
            cursor.moveToNext();
        }
        return item;
    }
}
