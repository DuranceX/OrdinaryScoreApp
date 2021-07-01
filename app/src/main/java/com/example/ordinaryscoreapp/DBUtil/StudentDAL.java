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
 * @description 学生表数据操作类
 */
public class StudentDAL{
    private String TABLE_NAME = "student";
    private Cursor cursor;
    private DBOpenHelper dbOpenHelper;
    private SQLiteDatabase db;

    public StudentDAL(Context context){
        dbOpenHelper = new DBOpenHelper(context,Constants.DatabaseName,null, Constants.DatabaseVersion);
        db = dbOpenHelper.getWritableDatabase();
    }

    /**
     * @author Xie Jiadi
     * @time 2021/7/1 9:02
     * @description 学生数据添加方法
     * @param no 学号
     * @param name 学生姓名
     * @param belongClass 学生班级
     */
    public long dbAdd(String no,String name,String belongClass){
        ContentValues values = new ContentValues();
        values.put("student_no",no);
        values.put("student_name",name);
        values.put("student_class",belongClass);
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
     * @description 学生数据删除方法
     * @param no 学生学号
     * @param name 学生姓名
     */
    public int dbDel(@Nullable String no,@Nullable String name){
        String where;
        if(!no.equals(""))
            where = "student_no='" + no + "'";
        else
            where = "student_name='" + name + "'";

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
     * @description 学生数据更新方法
     * @param no 学生学号
     * @param name 学生姓名
     * @param belongClass 学生所属班级
     */
    public long dbUpdate(String no,String name,String belongClass){
        ContentValues values = new ContentValues();
        values.put("student_name",name);
        values.put("student_class",belongClass);
        String where = "student_no='" + no + "'";
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
     * @description 查询所有学生数据
     */
    public Map[] dbFindAll(){
        cursor = db.query(TABLE_NAME, null, null, null, null, null, "student_no ASC");
        cursor.moveToFirst();
        Map item[] = new Map[cursor.getCount()];
        for(int i=0;i<cursor.getCount();i++)
            item[i] = new HashMap<String, Object>();
        int i=0;
        while(!cursor.isAfterLast()){
            String no = cursor.getString(0);
            String name = cursor.getString(1);
            String belongClass = cursor.getString(2);
            item[i].put("student_no",no);
            item[i].put("student_name",name);
            item[i++].put("student_class",belongClass);
            cursor.moveToNext();
        }
        return item;
    }



    /**
     * @author Xie Jiadi
     * @time 2021/7/1 9:09
     * @description 查询满足条件的学生数据
     * @param where 查询条件语句
     */
    public Map[] dbFind(String where){
        cursor = db.query(TABLE_NAME, null, where, null, null, null, "student_no ASC");
        cursor.moveToFirst();
        Map item[] = new Map[cursor.getCount()];
        if(cursor.getCount() == 0){
            Log.i("DataBase","没有查询到数据");
        }
        for(int i=0;i<cursor.getCount();i++)
            item[i] = new HashMap<String, Object>();
        int i=0;
        while(!cursor.isAfterLast()){
            String no = cursor.getString(0);
            String name = cursor.getString(1);
            String belongClass = cursor.getString(2);
            item[i].put("student_no",no);
            item[i].put("student_name",name);
            item[i++].put("student_class",belongClass);
            cursor.moveToNext();
        }
        return item;
    }


    /**
     * @author Xie Jiadi
     * @time 2021/7/1 16:36
     * @description 返回所有的班级
     */
    public Map[] dbGetClasses(){
        cursor = db.query(true,TABLE_NAME,new String[]{"student_class"},null,null,null,null,"student_class ASC",null);
        cursor.moveToFirst();
        Map item[] = new Map[cursor.getCount()];
        if(cursor.getCount() == 0){
            Log.i("DataBase","没有查询到数据");
        }
        for(int i=0;i<cursor.getCount();i++)
            item[i] = new HashMap<String, Object>();
        int i=0;
        while(!cursor.isAfterLast()){
            String className = cursor.getString(0);
            item[i++].put("className",className);
            cursor.moveToNext();
        }
        return item;
    }
}
