package com.example.ordinaryscoreapp.Course;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.ordinaryscoreapp.DBUtil.CheckInScoreDAL;
import com.example.ordinaryscoreapp.DBUtil.CourseDAL;
import com.example.ordinaryscoreapp.DBUtil.CourseScoreDAL;
import com.example.ordinaryscoreapp.DBUtil.CourseStudentDAL;
import com.example.ordinaryscoreapp.DBUtil.StudentDAL;
import com.example.ordinaryscoreapp.Model.Student;
import com.example.ordinaryscoreapp.R;
import com.example.ordinaryscoreapp.Widget.CourseStudentListViewAdapter;

import java.util.ArrayList;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CourseModify extends AppCompatActivity {
    EditText courseId;
    EditText courseTitle;
    EditText courseLocation;
    EditText courseTime;
    TextView addStudentButton;
    TextView delStudentButton;
    ImageView toolbarBackground;
    SearchView studentSearchBar;
    ListView studentListView;
    View dialogBackground;
    Button addButton;
    Button resetButton;
    Button delButton;
    View.OnClickListener listener;
    CourseStudentListViewAdapter courseStudentAdapter;
    ArrayList<String> studentsStringList;
    ArrayList<Student> students;
    ArrayList<Boolean> checked;
    CourseDAL courseDAL;
    StudentDAL studentDAL;
    CheckInScoreDAL checkInScoreDAL;
    CourseStudentDAL courseStudentDAL;
    CourseScoreDAL courseScoreDAL;

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
        studentListView = this.findViewById(R.id.courseStudentList);
        addButton = this.findViewById(R.id.courseAddButton);
        delButton = this.findViewById(R.id.courseDelButton);
        addStudentButton = this.findViewById(R.id.AddColumnButton);
        delStudentButton = this.findViewById(R.id.DelColumnButton);
        resetButton = this.findViewById(R.id.courseResetButton);
        studentSearchBar = this.findViewById(R.id.courseStudentSearchBar);
        students = new ArrayList<Student>();
        studentsStringList = new ArrayList<String>();
        checkInScoreDAL = new CheckInScoreDAL(this);
        studentDAL = new StudentDAL(this);
        courseStudentDAL = new CourseStudentDAL(this);
        courseScoreDAL = new CourseScoreDAL(this);
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
                case R.id.AddColumnButton:
                    dbAddStudent();
                    break;
                case R.id.DelColumnButton:
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

        studentSearchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                dbSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equals("")){
                    getStudentData(courseId.getText().toString());
                    showStudentList();
                }
                return false;
            }
        });

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
            }
        }
        //为空表示是添加课程信息,不做改动
        getStudentData(courseId.getText().toString());
        showStudentList();
    }


    /**
     * @author Xie Jiadi
     * @time 2021/7/1 10:25
     * @description 显示学生列表
     */
    private void showStudentList() {
        //获取学生信息
        if(studentsStringList.size() == 0)
            studentListView.setBackground(getDrawable(R.drawable.img_course_studentlist_empty));
        else
            studentListView.setBackground(null);
        courseStudentAdapter = new CourseStudentListViewAdapter(this,R.layout.widget_course_student_list,studentsStringList);
        //列表项点击事件
        studentListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                courseStudentAdapter.setClickItem(position);
                courseStudentAdapter.notifyDataSetChanged();
            }
        });
        studentListView.setAdapter(courseStudentAdapter);
    }

    /**
     * @author Xie Jiadi
     * @time 2021/6/30 10:50
     * @description 添加课程信息函数，调用courseDAl中的相关函数
     */
    public void dbAdd(){
        String id = courseId.getText().toString().trim();
        String title = courseTitle.getText().toString();
        String location = courseLocation.getText().toString();
        String time = courseTime.getText().toString();
        long result = courseDAL.dbAdd(id,title,location,time);
        if(result == -1){
            setResult(3);
            new SweetAlertDialog(this,SweetAlertDialog.ERROR_TYPE)
                .setTitleText("添加失败")
                .setContentText("未能添加 " + courseId.getText().toString())
                .setConfirmText("OK")
                .setConfirmClickListener(null)
                .show();
        }
        else {
            setResult(2);
            new SweetAlertDialog(this,SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("添加成功")
                .setContentText("成功添加 " + courseId.getText().toString())
                .setConfirmText("OK")
                .setConfirmClickListener(v -> {finish();})
                .show();
        }
    }

    /**
     * @author Xie Jiadi
     * @time 2021/6/30 10:52
     * @description 删除课程函数，调用courseDAl中的相关函数
     */
    public void dbDel(){
        new SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE)
            .setTitleText("你确定要删除该课程吗？")
            .setContentText(courseTitle.getText().toString())
            .setConfirmText("确定")
            .setConfirmClickListener(sweetAlertDialog -> {
                String id = courseId.getText().toString().trim();
                int result = courseDAL.dbDel(id,null);
                courseScoreDAL.dbDel(id,null);
                courseStudentDAL.dbDel(id,null);
                if(result > 0){
                    setResult(4);
                    sweetAlertDialog
                        .setTitleText("删除成功")
                        .setContentText("已成功删除该课程")
                        .setConfirmText("OK")
                        .setConfirmClickListener(v -> {finish();})
                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                }
                else{
                    setResult(5);
                    sweetAlertDialog
                        .setTitleText("删除失败")
                        .setContentText("未能成功删除该课程")
                        .setConfirmText("OK")
                        .setConfirmClickListener(null)
                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                }
            })
            .show();
    }

    /**
     * @author Xie Jiadi
     * @time 2021/6/30 11:01
     * @description 更新课程信息函数，调用courseDAl中的相关函数
     */
    public void dbUpdate(){
        String id = courseId.getText().toString().trim();
        String title = courseTitle.getText().toString();
        String location = courseLocation.getText().toString();
        String time = courseTime.getText().toString();
        long result = courseDAL.dbUpdate(id,title,location,time);
        if(result == -1){
            setResult(7);
            new SweetAlertDialog(this,SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("更新失败")
                    .setContentText("未能更新 " + courseId.getText().toString() + "的信息")
                    .setConfirmText("OK")
                    .setConfirmClickListener(null)
                    .show();
        }
        else {
            setResult(6);
            new SweetAlertDialog(this,SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("更新成功")
                .setContentText("成功更新 " + courseId.getText().toString() + "的信息")
                .setConfirmText("OK")
                .setConfirmClickListener(v -> {finish();})
                .show();
        }
    }


    /**
     * @author Xie Jiadi
     * @time 2021/7/1 17:30
     * @description 弹出对话框进行学生的添加，学生来自学生表
     */
    public void dbAddStudent(){
        String isExisted = "course_id='" + courseId.getText().toString().trim() + "'";
        if(courseDAL.dbFind(isExisted).length !=0){
            //定义变量
            Map[] classesMap;
            String[] classes;
            Spinner studentClass;
            Spinner studentName;
            ArrayAdapter<String> classAdapter;
            dialogBackground = LayoutInflater.from(this).inflate(R.layout.widget_course_student_adddialog,null);
            studentClass = dialogBackground.findViewById(R.id.dialogStudentClassSpinner);
            studentName = dialogBackground.findViewById(R.id.dialogStudentNameSpinner);

            //查询班级数，并绑定给studentClassSpinner
            classesMap = studentDAL.dbGetClasses();
            classes = new String[classesMap.length];
            for(int i=0;i<classesMap.length;i++)
                classes[i] = classesMap[i].get("className").toString();
            classAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,classes);
            classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
            studentClass.setAdapter(classAdapter);

            //Spinner二级联动
            studentClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                Map[] nameMap;
                String[] names;
                ArrayAdapter<String> nameAdapter;
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String where = "student_class='" + studentClass.getSelectedItem().toString() + "'";
                    nameMap = studentDAL.dbFind(where);
                    names = new String[nameMap.length+1];
                    names[0] = "全部";
                    for (int i = 1; i < nameMap.length+1; i++)
                        names[i] = nameMap[i-1].get("student_name").toString();
                    nameAdapter = new ArrayAdapter<String>(CourseModify.this, android.R.layout.simple_spinner_item,names);
                    nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                    studentName.setAdapter(nameAdapter);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            //跳出对话框进行操作
            new SweetAlertDialog(this,SweetAlertDialog.NORMAL_TYPE)
                    .setConfirmText("确定添加")
                    .setConfirmClickListener(sweetAlertDialog -> {
                        String no;
                        String where;
                        long result=-1;
                        if(studentName.getSelectedItem().toString().equals("全部"))
                            where = "student_class='" + studentClass.getSelectedItem().toString() + "'";
                        else
                            where = "student_name='" + studentName.getSelectedItem().toString() +
                                    "' and student_class='" + studentClass.getSelectedItem().toString() + "'";
                        Map[] temp = studentDAL.dbFind(where);
                        for(int i=0;i<temp.length;i++){
                            no = temp[i].get("student_no").toString();
                            result = courseStudentDAL.dbAdd(courseId.getText().toString().trim(),no,null);
                        }
                        sweetAlertDialog.dismiss();
                        if(result == -1){
                            new SweetAlertDialog(this,SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("添加失败")
                                    .setConfirmText("OK")
                                    .setConfirmClickListener(null)
                                    .show();
                        }
                        else {
                            for(int i=0;i<temp.length;i++){
                                no = temp[i].get("student_no").toString();
                                courseScoreDAL.dbAdd(courseId.getText().toString().trim(),no);
                            }
                            new SweetAlertDialog(this,SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("添加成功")
                                    .setConfirmText("OK")
                                    .setConfirmClickListener(null)
                                    .show();
                        }
                        getStudentData(courseId.getText().toString().trim());
                        showStudentList();
                    })
                    .setCustomView(dialogBackground)
                    .show();
        }
        else {
            new SweetAlertDialog(this,SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("请先添加课程再添加学生!")
                    .setConfirmText("ok")
                    .show();
        }
    }


    /**
     * @author Xie Jiadi
     * @time 2021/7/1 17:35
     * @description 删除选中的学生信息
     */
    public void dbDelStudent(){
        String isExisted = "course_id='" + courseId.getText().toString().trim() + "'";
        if(courseDAL.dbFind(isExisted).length !=0) {
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("确定要删除选中的学生吗？")
                    .setConfirmText("确定")
                    .setConfirmClickListener(sweetAlertDialog -> {
                        String id = courseId.getText().toString().trim();
                        int result = 0;
                        checked = courseStudentAdapter.getChecked();
                        for (int i = 0; i < checked.size(); i++) {
                            if (checked.get(i)) {
                                courseScoreDAL.dbDel(id,students.get(i).getNo());
                                result = courseStudentDAL.dbDel(id, students.get(i).getNo());
                            }
                        }
                        if (result > 0) {
                            sweetAlertDialog
                                    .setTitleText("删除成功")
                                    .setConfirmText("OK")
                                    .setConfirmClickListener(null)
                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        } else {
                            sweetAlertDialog
                                    .setTitleText("删除失败")
                                    .setConfirmText("OK")
                                    .setConfirmClickListener(null)
                                    .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        }
                        getStudentData(courseId.getText().toString());
                        showStudentList();
                    })
                    .show();
        }
        else{
            new SweetAlertDialog(this,SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("请先添加课程!")
                    .setConfirmText("ok")
                    .show();
        }
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
        studentsStringList.clear();
        students.clear();
        String where = "course_id = '" + ID.trim() + "'";
        Map[] studentListItems = courseStudentDAL.dbFind(where);
        for(int i=0;i<studentListItems.length;i++){
            where = "student_no='" + studentListItems[i].get("student_no") + "'";
            Map[] temp = studentDAL.dbFind(where);
            String no = (String)temp[0].get("student_no");
            String name = (String)temp[0].get("student_name");
            String belongClass = (String)temp[0].get("student_class");
            studentsStringList.add(no + "  " + name + "  " + belongClass);
            students.add(new Student(no,name,belongClass));
        }
    }


    /**
     * @author Xie Jiadi
     * @time 2021/7/3 11:18
     * @description 搜索选课学生列表
     * @param query 搜索用的sql语句
     */
    public void dbSearch(String query){
        ArrayList<String> tempStudentStringList = new ArrayList<String>(studentsStringList);
        ArrayList<Student> tempStudentList = new ArrayList<Student>(students);
        studentsStringList.clear();
        students.clear();
        for(int i=0;i<tempStudentStringList.size();i++){
            if(!query.equals("") && tempStudentStringList.get(i).contains(query)){
                studentsStringList.add(tempStudentStringList.get(i));
                students.add(tempStudentList.get(i));
            }
        }
        showStudentList();
    }
}
