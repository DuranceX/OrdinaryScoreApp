package com.example.ordinaryscoreapp.Score;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ordinaryscoreapp.DBUtil.CourseDAL;
import com.example.ordinaryscoreapp.R;
import com.example.ordinaryscoreapp.Widget.CourseListViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseScoreOverview extends AppCompatActivity {

    private List<Map<String,Object>> courseListItems = new ArrayList<Map<String, Object>>();
    private ListView courseList;
    private CourseListViewAdapter adapter;
    private CourseDAL courseDAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_score_overview);
        courseList = this.findViewById(R.id.courseList);
        courseDAL = new CourseDAL(this);

        showCourseList();
    }

    /**
     * @author Xie Jiadi
     * @time 2021/6/29 16:27
     * @description 显示课程数据
     */
    public void showCourseList(){
        courseListItems.clear();
        Map courseItems[] = courseDAL.dbFindAll();
        if(courseItems.length == 0){
            Toast.makeText(this,"没有查询到数据",Toast.LENGTH_SHORT).show();
        }
        else{
            for(int i=0;i<courseItems.length;i++){
                String title = (String) courseItems[i].get("course_title");
                String location = (String) courseItems[i].get("course_location");
                String time = (String) courseItems[i].get("course_time");
                Map item = new HashMap<String, Object>();
                item.put("title",title);
                item.put("location",location);
                item.put("time",time);
                courseListItems.add(item);
            }
        }
        adapter = new CourseListViewAdapter(this,courseListItems);
        courseList.setAdapter(adapter);
    }
}