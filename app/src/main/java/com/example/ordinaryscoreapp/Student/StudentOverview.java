package com.example.ordinaryscoreapp.Student;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ordinaryscoreapp.DBUtil.CourseScoreDAL;
import com.example.ordinaryscoreapp.DBUtil.CourseStudentDAL;
import com.example.ordinaryscoreapp.DBUtil.StudentDAL;
import com.example.ordinaryscoreapp.Model.Student;
import com.example.ordinaryscoreapp.R;
import com.example.ordinaryscoreapp.Score.CourseHomeworkScore;
import com.example.ordinaryscoreapp.Widget.CourseStudentListViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class StudentOverview extends AppCompatActivity {

    ListView studentListView;
    View addButton;
    View delButton;
    SearchView studentSearchBar;
    EditText noEditText;
    EditText nameEditText;
    EditText classEditText;
    ArrayList<String> studentsStringList;
    StudentDAL studentDAL;
    CourseStudentDAL courseStudentDAL;
    CourseScoreDAL courseScoreDAL;
    ArrayAdapter studentAdapter;
    Map[] students;

    ActionBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_overview);

        studentListView = this.findViewById(R.id.StudentList);
        addButton = this.findViewById(R.id.StudentAddButton);
        delButton = this.findViewById(R.id.StudentDelButton);
        studentSearchBar = this.findViewById(R.id.StudentSearchBar);
        studentDAL = new StudentDAL(this);
        courseStudentDAL = new CourseStudentDAL(this);
        courseScoreDAL = new CourseScoreDAL(this);
        studentsStringList = new ArrayList<>();

        delButton.setOnClickListener(v -> {
            View dialogBackground = LayoutInflater.from(this).inflate(R.layout.widget_student_modify_dialog, null);
            noEditText = dialogBackground.findViewById(R.id.StudentDialogNoEditText);
            nameEditText = dialogBackground.findViewById(R.id.StudentDialogNameEditText);
            dialogBackground.findViewById(R.id.StudentDialogClassEditText).setVisibility(View.INVISIBLE);
            dialogBackground.findViewById(R.id.StudentDialogClassLabel).setVisibility(View.INVISIBLE);
            new SweetAlertDialog(this,SweetAlertDialog.NORMAL_TYPE)
                    .setCustomView(dialogBackground)
                    .setConfirmText("ok")
                    .setConfirmClickListener(sweetAlertDialog ->{
                        String no = noEditText.getText().toString();
                        String name = nameEditText.getText().toString();
                        studentDAL.dbDel(no,name);
                        courseStudentDAL.dbDel(null,no);
                        courseScoreDAL.dbDel(null,no);
                        getStudentData();
                        showStudentList();
                        sweetAlertDialog.dismiss();
                    })
                    .show();
        });
        addButton.setOnClickListener(v -> {
            View dialogBackground = LayoutInflater.from(this).inflate(R.layout.widget_student_modify_dialog, null);
            noEditText = dialogBackground.findViewById(R.id.StudentDialogNoEditText);
            nameEditText = dialogBackground.findViewById(R.id.StudentDialogNameEditText);
            classEditText = dialogBackground.findViewById(R.id.StudentDialogClassEditText);
            new SweetAlertDialog(this,SweetAlertDialog.NORMAL_TYPE)
                    .setCustomView(dialogBackground)
                    .setConfirmText("ok")
                    .setConfirmClickListener(sweetAlertDialog ->{
                        String no = noEditText.getText().toString();
                        String name = nameEditText.getText().toString();
                        String belongClass = classEditText.getText().toString();
                        studentDAL.dbAdd(no,name,belongClass);
                        getStudentData();
                        showStudentList();
                        sweetAlertDialog.dismiss();
                    })
                    .show();
        });

        studentSearchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                dbSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equals("")){
                    getStudentData();
                    showStudentList();
                }
                return false;
            }
        });

        //设置返回按钮和标题基本样式
        bar = getSupportActionBar();
        bar.setHomeButtonEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setTitle("学生管理");

        getStudentData();
        showStudentList();
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
     * @time 2021/7/5 16:22
     * @description 查询学生表，获取所有学生信息
     */
    public void getStudentData(){
        studentsStringList.clear();
        students = studentDAL.dbFindAll();
        for(int i=0;i<students.length;i++){
            String no = (String)students[i].get("student_no");
            String name = (String)students[i].get("student_name");
            String belongClass = (String)students[i].get("student_class");
            studentsStringList.add(no + "  " + name + "  " + belongClass);
        }
        studentAdapter = new ArrayAdapter<>(this,R.layout.widget_student_list,studentsStringList);
    }


    /**
     * @author Xie Jiadi
     * @time 2021/7/5 16:23
     * @description 更新显示学生列表
     */
    private void showStudentList() {
        //获取学生信息
        if(studentsStringList.size() == 0)
            studentListView.setBackground(getDrawable(R.drawable.img_course_studentlist_empty));
        else
            studentListView.setBackground(null);

        //列表项点击事件
        studentListView.setOnItemClickListener((parent, view, position, id) -> {
            View dialogBackground = LayoutInflater.from(this).inflate(R.layout.widget_student_modify_dialog, null);
            noEditText = dialogBackground.findViewById(R.id.StudentDialogNoEditText);
            nameEditText = dialogBackground.findViewById(R.id.StudentDialogNameEditText);
            classEditText = dialogBackground.findViewById(R.id.StudentDialogClassEditText);
            noEditText.setText(students[position].get("student_no").toString());
            noEditText.setFocusable(false);
            nameEditText.setText(students[position].get("student_name").toString());
            classEditText.setText(students[position].get("student_class").toString());
            new SweetAlertDialog(this,SweetAlertDialog.NORMAL_TYPE)
                    .setCustomView(dialogBackground)
                    .setConfirmText("ok")
                    .setConfirmClickListener(v ->{
                        String no = noEditText.getText().toString();
                        String name = nameEditText.getText().toString();
                        String belongClass = classEditText.getText().toString();
                        studentDAL.dbUpdate(no,name,belongClass);
                        getStudentData();
                        showStudentList();
                        v.dismiss();
                    })
                    .show();
        });
        studentListView.setAdapter(studentAdapter);
    }


    public void dbSearch(String query){
        ArrayList<String> tempStudentStringList = new ArrayList<String>(studentsStringList);
        studentsStringList.clear();
        for(int i=0;i<tempStudentStringList.size();i++){
            if(!query.equals("") && tempStudentStringList.get(i).contains(query)){
                studentsStringList.add(tempStudentStringList.get(i));
            }
        }
        showStudentList();
    }
}
