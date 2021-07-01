package com.example.ordinaryscoreapp.Course;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.ordinaryscoreapp.DBUtil.CourseDAL;
import com.example.ordinaryscoreapp.DBUtil.CourseStudentDAL;
import com.example.ordinaryscoreapp.DBUtil.StudentDAL;
import com.example.ordinaryscoreapp.R;
import com.example.ordinaryscoreapp.Widget.CourseStudentListViewAdapter;

import java.util.ArrayList;
import java.util.Map;

public class CourseModify extends AppCompatActivity {
    EditText courseId;
    EditText courseTitle;
    EditText courseLocation;
    EditText courseTime;
    TextView addStudentButton;
    TextView delStudentButton;
    ImageView toolbarBackground;
    ListView studentList;
    Button addButton;
    Button resetButton;
    Button delButton;
    View.OnClickListener listener;
    ArrayAdapter courseStudentAdapter;
    ArrayList<String> students;
    CourseDAL courseDAL;
    StudentDAL studentDAL;
    CourseStudentDAL courseStudentDAL;

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
        courseTitle = this.findViewById(R.id.courseTitleEditText);
        courseLocation = this.findViewById(R.id.courseLocationEditText);
        courseTime = this.findViewById(R.id.courseTimeEditText);
        studentList = this.findViewById(R.id.courseStudentList);
        addButton = this.findViewById(R.id.courseAddButton);
        delButton = this.findViewById(R.id.courseDelButton);
        addStudentButton = this.findViewById(R.id.courseAddStudent);
        delStudentButton = this.findViewById(R.id.courseDelStudent);
        resetButton = this.findViewById(R.id.courseResetButton);
        students = new ArrayList<String>();
        studentDAL = new StudentDAL(this);
        courseStudentDAL = new CourseStudentDAL(this);
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
                case R.id.courseAddStudent:
                    dbAddStudent();
                    break;
                case R.id.courseDelStudent:
                    dbDelStudent();
                    break;
            }
        };
        addButton.setOnClickListener(listener);
        resetButton.setOnClickListener(listener);
        delButton.setOnClickListener(listener);
        addStudentButton.setOnClickListener(listener);
        delStudentButton.setOnClickListener(listener);

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
                courseId.setText(" " + item[0].get("course_id").toString());
                courseId.setFocusable(false);
                courseTitle.setText(item[0].get("course_title").toString());
                courseLocation.setText(item[0].get("course_location").toString());
                courseTime.setText(item[0].get("course_time").toString());
                addButton.setText("修改信息");
                int imageID = getResources().getIdentifier(imageName,"drawable",getPackageName());
                toolbarBackground.setImageResource(imageID);
                bar.setSubtitle(courseTitle.getText().toString());
                showStudentList();
            }
        }
        //为空表示是添加课程信息,不做改动
    }


    /**
     * @author Xie Jiadi
     * @time 2021/7/1 10:25
     * @description 显示学生列表
     */
    private void showStudentList() {
        //获取学生信息
        students.clear();
        getStudentData(courseId.getText().toString());
        if(students.size() == 0)
            studentList.setBackground(getDrawable(R.drawable.img_course_studentlist_empty));
        else
            studentList.setBackground(null);
        courseStudentAdapter = new CourseStudentListViewAdapter(this,R.layout.widget_course_student_list,students);
        studentList.setAdapter(courseStudentAdapter);
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


    public void dbAddStudent(){
        //AddMethod
        showStudentList();
    }


    public void dbDelStudent(){

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


    /**
     * @author Xie Jiadi
     * @time 2021/7/1 10:15
     * @description 通过课程ID来查询多个表，得到选了该课的所有学生的信息
     * @param ID 课程ID
     */
    public void getStudentData(String ID){
        String where = "course_id = '" + ID + "'";
        Map[] studentListItems = courseStudentDAL.dbFind(where);
        for(int i=0;i<studentListItems.length;i++){
            where = "student_no='" + studentListItems[i].get("student_no") + "'";
            Map[] temp = studentDAL.dbFind(where);
            students.add(temp[0].get("student_no") + "  " +
                    temp[0].get("student_name") + "  " +
                    temp[0].get("student_class"));
        }
    }
}
