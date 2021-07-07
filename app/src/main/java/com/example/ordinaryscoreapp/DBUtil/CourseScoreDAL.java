package com.example.ordinaryscoreapp.DBUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.ordinaryscoreapp.Model.Constants;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CourseScoreDAL {

    private final String TABLE_NAME = "course_score";
    private Cursor cursor;
    private DBOpenHelper dbOpenHelper;
    private SQLiteDatabase db;
    private StudentDAL studentDAL;

    public CourseScoreDAL(Context context){
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
        String[] course_ids = {"CS1001","CS1002"};
        String[] student_nos = {"3180608016","3180608031","3180608045","3180608096"};
        studentDAL.dbAdd("3180608016","XXX","软件1802");
        studentDAL.dbAdd("3180608031","EEE","软件1802");
        studentDAL.dbAdd("3180608045","SSS","软件1801");
        studentDAL.dbAdd("3180608096","RRR","软件1803");
        for(int i = 0; i< course_ids.length;i++){
            ContentValues values = new ContentValues();
            values.put("course_id",course_ids[i]);
            for(int j=0;j<student_nos.length;j++){
                values.put("student_no",student_nos[j]);
                db.insert(TABLE_NAME,null,values);
            }
        }
        ContentValues values = new ContentValues();
        for(int i=1;i<=5;i++){
            values.put("checkin_no_" + i,"");
            values.put("homework_no_"+i,"");
            values.put("program_no_"+i,"");
        }
        db.update(TABLE_NAME,values,null,null);
    }


    /**
     * @author Xie Jiadi
     * @time 2021/7/3 11:19
     * @description 添加数据，当添加课程中的选课信息时，同步添加课程成绩表
     * @param id 课程编号
     * @param no 学生学号
     */
    public long dbAdd(String id,String no){
        ContentValues values = new ContentValues();
        values.put("course_id",id);
        values.put("student_no",no);
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
     * @time 2021/7/3 11:20
     * @description 删除课程成绩表的记录，当学号学号为空时，表示删除的是这个课程，即删除所有的该课程的课程成绩记录
     * @param id 课程编号
     * @param no 学生学号
     */
    public int dbDel(@Nullable String id, @Nullable String no){
        String where;
        if(id!=null && no!=null)
            where="course_id='" + id + "' and student_no='" + no + "'";
        else if(id == null)
            where = "student_no='" + no + "'";
        else
            where = "course_id='" + id +"'";
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
     * @time 2021/7/4 16:21
     * @description 查询对应课程的选课成绩信息
     * @param where 条件语句
     */
    public ArrayList<Map<String,Object>> dbFind(String where){
        cursor = db.query(TABLE_NAME, null, where, null, null, null, "student_no ASC");
        cursor.moveToFirst();
        ArrayList<Map<String,Object>> items  = new ArrayList<>();
        if(cursor.getCount() == 0){
            Log.i("DataBase","没有查询到数据");
        }
        while(!cursor.isAfterLast()){
            Map<String,Object> item = new LinkedHashMap<>();
            String course_id = cursor.getString(1);
            String student_no = cursor.getString(2);
            String studentWhere = "student_no='" + student_no + "'";
            String student_name = studentDAL.dbFind(studentWhere)[0].get("student_name").toString();
            item.put("course_id",course_id);
            item.put("student_no",student_no);
            item.put("student_name",student_name);
            int p=1;
            String totalScore="";
            double checkInCount=0;
            double homeworkScore=0;
            double programScore=0;
            for(int j=3;j<cursor.getColumnCount();j++) {
                if (cursor.getColumnName(j).contains("checkin")) {
                    item.put("checkin_no_" + p++, cursor.getString(j) == null ? "" : cursor.getString(j));
                    if (cursor.getString(j).equals("√"))
                        checkInCount = checkInCount + 1;
                }
            }
            p=1;
            for (int j=3;j<cursor.getColumnCount();j++) {
                if (cursor.getColumnName(j).contains("homework")) {
                    item.put("homework_no_" + p++, cursor.getString(j) == null ? "" : cursor.getString(j));
                    if (cursor.getString(j) != null && !cursor.getString(j).equals(""))
                        homeworkScore += Float.parseFloat(cursor.getString(j));
                }
            }
            p=1;
            for(int j=3;j<cursor.getColumnCount();j++) {
                if (cursor.getColumnName(j).contains("program")) {
                    item.put("program_no_" + p++, cursor.getString(j) == null ? "" : cursor.getString(j));
                    if (cursor.getString(j) != null && !cursor.getString(j).equals(""))
                        programScore += Float.parseFloat(cursor.getString(j));
                }
            }
            for(int j=3;j<cursor.getColumnCount();j++){
                if(cursor.getColumnName(j).equals("totalScore")){
                    checkInCount = checkInCount/Constants.CheckInColumnNumber * 20;
                    homeworkScore = homeworkScore/(Constants.HomeworkColumnNumber*10) * 30;
                    programScore = programScore/(Constants.ProgramColumnNumber*10) * 50;
                    totalScore = checkInCount + homeworkScore + programScore + "";
                    ContentValues values = new ContentValues();
                    values.put("totalScore",totalScore);
                    String updateWhere = "student_no='" + student_no + "' and course_id='" + course_id + "'";
                    db.update(TABLE_NAME,values,updateWhere,null);
                    item.put("totalScore",totalScore);
                }
            }
            items.add(item);
            cursor.moveToNext();
        }
        return items;
    }

    /**
     * @author Xie Jiadi
     * @time 2021/7/4 18:46
     * @description 为数据库新增一条列
     * @param columnName 要增加的列名
     */
    public void addColumn(String columnName){
        Log.i("DataBase","调用addColumn方法");
        String sql = "Alter TABLE course_score add column " + columnName;
        db.execSQL(sql);
    }


    /**
     * @author Xie Jiadi
     * @time 2021/7/4 18:46
     * @description 为数据库删除一条列
     * @param columnName 要删除的列名
     * @param context 用于新建数据库链接的context
     */
    public void delColumn(String columnName,Context context){
        Log.i("DataBase","调用delColumn方法");
        //在删除列后是一个新的同名数据库，需要重新获取，如果在这里不进行重新赋值的话，在第二次执行删除库操作时就会闪退
        dbOpenHelper = new DBOpenHelper(context, Constants.DatabaseName,null, Constants.DatabaseVersion);
        db = dbOpenHelper.getWritableDatabase();
        //查询原表数据
        cursor = db.query(TABLE_NAME, null, null, null, null, null, "student_no ASC");

        //根据原表新建一个表
        String sql = "create table new_course_score as select _id";
        for(int i=1;i<cursor.getColumnCount();i++){
            if(!cursor.getColumnName(i).equals(columnName))
                sql += "," + cursor.getColumnName(i);
        }
        sql += " from course_score";
        db.execSQL(sql);

        //删除旧表
        sql = "drop table course_score";
        db.execSQL(sql);

        //重命名新表
        sql = "alter table new_course_score rename to course_score";
        db.execSQL(sql);
    }
}
