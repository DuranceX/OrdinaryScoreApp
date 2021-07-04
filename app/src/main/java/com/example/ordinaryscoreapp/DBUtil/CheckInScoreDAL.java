package com.example.ordinaryscoreapp.DBUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.ordinaryscoreapp.Model.Constants;
import com.example.ordinaryscoreapp.Model.Student;

import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Xie Jiadi
 * @time 2021/7/3 11:19
 * @description 考勤分数数据操作类
 */
public class CheckInScoreDAL {
    private final String TABLE_NAME = "course_score";
    private final String COLUMN_NAME = "checkin";
    private Cursor cursor;
    private DBOpenHelper dbOpenHelper;
    private SQLiteDatabase db;
    private StudentDAL studentDAL;

    public CheckInScoreDAL(Context context){
        studentDAL = new StudentDAL(context);
        dbOpenHelper = new DBOpenHelper(context, Constants.DatabaseName,null, Constants.DatabaseVersion);
        db = dbOpenHelper.getWritableDatabase();
    }


    /**
     * @author Xie Jiadi
     * @time 2021/7/3 11:21
     * @description 更新课程成绩信息
     * @param scores 一个Map对象的List，List的大小即总记录数，每个Map存放一条记录的信息
     */
    public long dbUpdate(List<Map<String, Object>> scores){
        long result=-1;
        for(int i=0;i<scores.size();i++){
            ContentValues values = new ContentValues();
            String course_id = scores.get(i).get("course_id").toString();
            String student_no = scores.get(i).get("student_no").toString();
            for(int j=3;j<scores.get(i).size();j++){
                String column_name = COLUMN_NAME+"_no_" + (j-2) + "";
                String score = scores.get(i).get(column_name).toString();
                values.put(column_name,score);
            }
            String where = "student_no='" + student_no + "' and course_id='" + course_id + "'";
            result = db.update(TABLE_NAME,values,where,null);
            if(result > 0){
                Log.i("DataBase","updateSucceed");
            }
            else
                Log.i("DataBase","updateFailed");
        }
        return result;
    }


    /**
     * @author Xie Jiadi
     * @time 2021/7/3 11:22
     * @description 查询数据表并返回信息
     * @param where 查询语句
     * @return items 一个Map对象的List，List的大小即总记录数，每个Map存放一条记录的信息
     */
    public ArrayList<Map<String,Object>> dbFind(String where){
        cursor = db.query(TABLE_NAME, null, where, null, null, null, "student_no ASC");
        cursor.moveToFirst();
        ArrayList<Map<String,Object>> items  = new ArrayList<>();
        if(cursor.getCount() == 0){
            Log.i("DataBase","没有查询到数据");
        }
        int columnCount=0;
        while(!cursor.isAfterLast()){
            columnCount=0;
            Map<String,Object> item = new LinkedHashMap<>();
            String course_id = cursor.getString(1);
            String student_no = cursor.getString(2);
            String studentWhere = "student_no='" + student_no + "'";
            String student_name = studentDAL.dbFind(studentWhere)[0].get("student_name").toString();
            item.put("course_id",course_id);
            item.put("student_no",student_no);
            item.put("student_name",student_name);
            int p=1;
            for(int j=3;j<cursor.getColumnCount();j++){
               if(cursor.getColumnName(j).contains(COLUMN_NAME)) {
                   item.put(COLUMN_NAME + "_no_" + p++, cursor.getString(j) == null ? "" : cursor.getString(j));
                   columnCount++;
               }
            }
            items.add(item);
            cursor.moveToNext();
        }
        Constants.CheckInColumnNumber = columnCount;
        return items;
    }

}
