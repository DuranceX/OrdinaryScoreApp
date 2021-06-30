package com.example.ordinaryscoreapp.Course;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
    ActionBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_overview);
        courseList = this.findViewById(R.id.courseList);
        courseDAL = new CourseDAL(this);

        //设置返回按钮和标题基本样式
        bar = getSupportActionBar();
        bar.setHomeButtonEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setTitle("当前课程");
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
            Toast.makeText(this,"没有查询到数据",Toast.LENGTH_SHORT).show();
        }
        else{
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


    /**
     * @author Xie Jiadi
     * @time 2021/6/30 10:02
     * @description 得到CourseModify传递的数据后执行的操作
     * @param requestCode 0：添加课程事件  1：修改课程信息事件
     * @param resultCode 2：添加事件成功 3：添加事件失败 4：删除事件成功 5：删除事件失败 6：更新事件成功 7：更新事件失败
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case 2:
                Toast.makeText(this,"添加成功",Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(this,"添加失败",Toast.LENGTH_SHORT).show();
                break;
            case 4:
                Toast.makeText(this,"删除成功",Toast.LENGTH_SHORT).show();
                break;
            case 5:
                Toast.makeText(this,"删除失败",Toast.LENGTH_SHORT).show();
                break;
            case 6:
                Toast.makeText(this,"更新成功",Toast.LENGTH_SHORT).show();
                break;
            case 7:
                Toast.makeText(this,"更新失败",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}