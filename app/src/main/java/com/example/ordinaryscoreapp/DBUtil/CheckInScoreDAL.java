package com.example.ordinaryscoreapp.DBUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.ordinaryscoreapp.Model.Constants;
import com.example.ordinaryscoreapp.Model.Student;

import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CheckInScoreDAL {
    private String TABLE_NAME = "course_score";
    private Cursor cursor;
    private DBOpenHelper dbOpenHelper;
    private SQLiteDatabase db;
    private StudentDAL studentDAL;

    public CheckInScoreDAL(Context context){
        studentDAL = new StudentDAL(context);
        dbOpenHelper = new DBOpenHelper(context, Constants.DatabaseName,null, Constants.DatabaseVersion);
        db = dbOpenHelper.getWritableDatabase();
    }


    public void dbInitial(){
//        String sql = "drop table course_score";
//        db.execSQL(sql);
//        sql = "create table course_score(_id integer primary key autoincrement," +
//                "course_id varchar," +
//                "student_no varchar," +
//                "checkin_no_1 varchar," +
//                "checkin_no_2 varchar," +
//                "checkin_no_3 varchar," +
//                "checkin_no_4 varchar," +
//                "checkin_no_5 varchar," +
//                "homework_no_1 varchar," +
//                "homework_no_2 varchar," +
//                "homework_no_3 varchar," +
//                "homework_no_4 varchar," +
//                "homework_no_5 varchar," +
//                "program_no_1 varchar," +
//                "program_no_2 varchar," +
//                "program_no_3 varchar," +
//                "program_no_4 varchar," +
//                "program_no_5 varchar," +
//                "totalScore varchar," +
//                "foreign key(course_id) references course(course_id)," +
//                "foreign key(student_no) references student(student_no))";
//        db.execSQL(sql);
//        String[] course_ids = {"CS1001","CS1002"};
//        String[] student_nos = {"3180608016","3180608031","3180608045","3180608096"};
//        for(int i = 0; i< course_ids.length;i++){
//            ContentValues values = new ContentValues();
//            values.put("course_id",course_ids[i]);
//            for(int j=0;j<student_nos.length;j++){
//                values.put("student_no",student_nos[j]);
//                db.insert(TABLE_NAME,null,values);
//            }
//        }
        ContentValues values = new ContentValues();
        for(int i=1;i<=5;i++){
            values.put("checkin_no_" + i,"");
            values.put("homework_no_"+i,"");
            values.put("program_no_"+i,"");
        }
        db.update(TABLE_NAME,values,null,null);
    }

    public long dbUpdate(List<Map<String, Object>> scores){
        long result=-1;
        for(int i=0;i<scores.size();i++){
            ContentValues values = new ContentValues();
            String course_id = scores.get(i).get("course_id").toString();
            String student_no = scores.get(i).get("student_no").toString();
            for(int j=3;j<scores.get(i).size();j++){
                String column_name = "checkin_no_" + (j-2) + "";
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


    public ArrayList<Map<String,Object>> dbFind(String where){
        cursor = db.query(TABLE_NAME, null, where, null, null, null, "student_no ASC");
        cursor.moveToFirst();
        ArrayList<Map<String,Object>> items  = new ArrayList<>();
        if(cursor.getCount() == 0){
            Log.i("DataBase","没有查询到数据");
        }
        int i=0;
        while(!cursor.isAfterLast()){
            Map<String,Object> item = new LinkedHashMap<>();
            String course_id = cursor.getString(1);
            String student_no = cursor.getString(2);
            String studentWhere = "student_no='" + student_no + "'";
            String student_name = studentDAL.dbFind(studentWhere)[0].get("student_name").toString();
            item.put("course_id",course_id);
            item.put("student_no",student_no);
            item.put("student_name",student_name);
            for(int j=3;j<cursor.getColumnCount();j++){
               if(cursor.getColumnName(j).contains("checkin"))
                       item.put("checkin_no_" + (j-2),cursor.getString(j));
            }
            items.add(item);
            cursor.moveToNext();
        }
        return items;
    }

}
