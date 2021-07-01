package com.example.ordinaryscoreapp.Course;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.ordinaryscoreapp.DBUtil.CourseDAL;
import com.example.ordinaryscoreapp.R;
import com.example.ordinaryscoreapp.Widget.CourseListViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseOverview extends AppCompatActivity {

    private List<Map<String,Object>> courseListItems = new ArrayList<Map<String, Object>>();
    private ListView courseList;
    private CourseListViewAdapter adapter;
    private CourseDAL courseDAL;
    private SearchView searchView;
    ActionBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_overview);
        courseList = this.findViewById(R.id.courseList);
        searchView = this.findViewById(R.id.courseOverviewSearchBar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                dbSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equals("")){
                    showAllCourse();
                    showCourseList();
                }
                return false;
            }
        });

        courseList.setDivider(null);
        courseDAL = new CourseDAL(this);

        //设置返回按钮和标题基本样式
        bar = getSupportActionBar();
        bar.setHomeButtonEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setTitle("我的课程");
    }

    @Override
    protected void onResume() {
        Log.i("Activity","调用onResume()");
        showAllCourse();
        showCourseList();
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * @author Xie Jiadi
     * @time 2021/6/29 16:27
     * @description 查询所有课程数据
     */
    public void showAllCourse(){
        courseListItems.clear();
        Map courseItems[] = courseDAL.dbFindAll();
        if(courseItems.length == 0){
            courseList.setBackground(getDrawable(R.drawable.img_courselist_emptybackground));
            Toast.makeText(this,"没有查询到数据",Toast.LENGTH_SHORT).show();
        }
        else{
            courseList.setBackground(null);
            for(int i=0;i<courseItems.length;i++){
                String id = (String) courseItems[i].get("course_id");
                String title = (String) courseItems[i].get("course_title");
                String location = (String) courseItems[i].get("course_location");
                String time = (String) courseItems[i].get("course_time");
                Map item = new HashMap<String, Object>();
                item.put("id",id);
                item.put("title",title);
                item.put("location",location);
                item.put("time",time);
                courseListItems.add(item);
            }
        }
    }

    /**
     * @author Xie Jiadi
     * @time 2021/6/30 8:39
     * @description 显示课程列表数据
     */
    public void showCourseList(){
        adapter = new CourseListViewAdapter(this,courseListItems);
        courseList.setAdapter(adapter);
    }

    public void dbSearch(String query){
        courseListItems.clear();
        String where;
        where = "course_title like '%" + query + "%'";
        Map[] courseItems = courseDAL.dbFind(where);
        if(courseItems.length == 0){
            courseList.setBackground(getDrawable(R.drawable.img_courselist_emptybackground));
            Toast.makeText(this,"没有查询到数据",Toast.LENGTH_SHORT).show();
        }
        else{
            courseList.setBackground(null);
            for(int i=0;i<courseItems.length;i++){
                String id = (String) courseItems[i].get("course_id");
                String title = (String) courseItems[i].get("course_title");
                String location = (String) courseItems[i].get("course_location");
                String time = (String) courseItems[i].get("course_time");
                Map item = new HashMap<String, Object>();
                item.put("id",id);
                item.put("title",title);
                item.put("location",location);
                item.put("time",time);
                courseListItems.add(item);
            }
        }
        showCourseList();
    }

}