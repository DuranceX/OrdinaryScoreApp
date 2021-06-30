package com.example.ordinaryscoreapp.Course;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.ordinaryscoreapp.DBUtil.CourseDAL;
import com.example.ordinaryscoreapp.R;

import java.util.Map;

public class CourseModify extends AppCompatActivity {
    EditText courseId;
    EditText courseTitle;
    EditText courseLocation;
    EditText courseTime;
    ImageView toolbarBackground;
    Spinner[] timePicker;
    Button addButton;
    Button resetButton;
    Button delButton;
    View.OnClickListener listener;
    CourseDAL courseDAL;

    Toolbar bar;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_modify);

        //设置返回按钮和标题基本样式
        bar = this.findViewById(R.id.toolbar);
        toolbarBackground = this.findViewById(R.id.toolbarBackgroundImgView);
        setSupportActionBar(bar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bar.setTitle("当前课程");

        //绑定组件,初始化变量
        courseId = this.findViewById(R.id.courseIdEditText);
        courseId.setBackgroundColor(0xEEEEEEEE);
        courseTitle = this.findViewById(R.id.courseTitleEditText);
        courseLocation = this.findViewById(R.id.courseLocationEditText);
        courseTime = this.findViewById(R.id.courseTimeEditText);
        addButton = this.findViewById(R.id.courseAddButton);
        delButton = this.findViewById(R.id.courseDelButton);
        resetButton = this.findViewById(R.id.courseResetButton);
        courseDAL = new CourseDAL(this);
        listener = v -> {
            switch (v.getId()){
                case R.id.courseAddButton:
                    if(addButton.getText().toString().equals("添加课程"))
                        dbAdd();
                    else
                        dbUpdate();
                    break;
                case R.id.courseResetButton:
                    resetData();
                    break;
                case R.id.courseDelButton:
                    dbDel();
                    break;
            }
        };
        addButton.setOnClickListener(listener);
        resetButton.setOnClickListener(listener);
        delButton.setOnClickListener(listener);

        //填充页面数据
        setData();
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
     * @time 2021/6/30 10:43
     * @description 如果是修改课程的信息的话填充页面信息
     */
    public void setData(){
        //非空表示是修改课程信息
        if(!(getIntent().getSerializableExtra("id")==null)){
            String id = (String)getIntent().getSerializableExtra("id");
            String imageName = (String)getIntent().getSerializableExtra("background");
            String where = "course_id = '" + id + "'";
            Map[] item = courseDAL.dbFind(where);
            if(item != null){
                courseId.setText(item[0].get("course_id").toString());
                courseId.setFocusable(false);
                courseTitle.setText(item[0].get("course_title").toString());
                courseLocation.setText(item[0].get("course_location").toString());
                courseTime.setText(item[0].get("course_time").toString());
                addButton.setText("修改信息");
                int imageID = getResources().getIdentifier(imageName,"drawable",getPackageName());
                toolbarBackground.setImageResource(imageID);
                bar.setSubtitle(courseTitle.getText().toString());
            }
        }
        //为空表示是添加课程信息,不做改动
    }

    /**
     * @author Xie Jiadi
     * @time 2021/6/30 10:50
     * @description 添加课程信息函数，调用courseDAl中的相关函数
     */
    public void dbAdd(){
        String id = courseId.getText().toString();
        String title = courseTitle.getText().toString();
        String location = courseLocation.getText().toString();
        String time = courseTime.getText().toString();
        long result = courseDAL.dbAdd(id,title,location,time);
        if(result == -1){
            setResult(3);
        }
        else
            setResult(2);
        finish();
    }

    /**
     * @author Xie Jiadi
     * @time 2021/6/30 10:52
     * @description 删除课程函数，调用courseDAl中的相关函数
     */
    public void dbDel(){
        String id = courseId.getText().toString();
        int result = courseDAL.dbDel(id,null);
        if(result > 0)
            setResult(4);
        else
            setResult(5);
        finish();
    }

    /**
     * @author Xie Jiadi
     * @time 2021/6/30 11:01
     * @description 更新课程信息函数，调用courseDAl中的相关函数
     */
    public void dbUpdate(){
        String id = courseId.getText().toString();
        String title = courseTitle.getText().toString();
        String location = courseLocation.getText().toString();
        String time = courseTime.getText().toString();
        long result = courseDAL.dbUpdate(id,title,location,time);
        if(result == -1){
            setResult(7);
        }
        else
            setResult(6);
        finish();
    }

    /**
     * @author Xie Jiadi
     * @time 2021/6/30 11:11
     * @description 重设数据
     */
    public void resetData(){
        if(!(getIntent().getSerializableExtra("id")==null)){
            String id = (String)getIntent().getSerializableExtra("id");
            String where = "course_id = '" + id + "'";
            Map[] item = courseDAL.dbFind(where);
            if(item != null){
                courseId.setText(item[0].get("course_id").toString());
                courseId.setFocusable(false);
                courseTitle.setText(item[0].get("course_title").toString());
                courseLocation.setText(item[0].get("course_location").toString());
                courseTime.setText(item[0].get("course_time").toString());
                addButton.setText("修改信息");
            }
        }
        else{
            courseId.setText("");
            courseTitle.setText("");
            courseLocation.setText("");
            courseTime.setText("");
        }
    }
}
