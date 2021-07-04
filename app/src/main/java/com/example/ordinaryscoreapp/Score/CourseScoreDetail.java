package com.example.ordinaryscoreapp.Score;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.ordinaryscoreapp.R;

/**
 * @author Xie Jiadi
 * @time 2021/7/3 11:37
 * @description 课程的详细操作界面
 */
public class CourseScoreDetail extends AppCompatActivity {

    ImageView toolbarBackground;
    TextView toolbarCourseTitle;
    Toolbar bar;
    View.OnClickListener listener;
    View checkInButton;
    View homeworkButton;
    View programButton;
    View totalScoreButton;

    String id;
    String imageName;
    String title;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_score_detail);
        //绑定组件
        checkInButton = this.findViewById(R.id.checkInButton);
        homeworkButton = this.findViewById(R.id.homeworkButton);
        programButton = this.findViewById(R.id.programButton);
        totalScoreButton = this.findViewById(R.id.totalScoreButton);
        bar = this.findViewById(R.id.ScoreDetailToolbar);
        toolbarBackground = this.findViewById(R.id.ScoreDetailToolbarBackgroundImgView);
        toolbarCourseTitle = this.findViewById(R.id.CourseScoreToolbarCourseTitle);

        //初始化变量
        id = (String) getIntent().getSerializableExtra("id");
        imageName = (String)getIntent().getSerializableExtra("background");
        title = (String)getIntent().getSerializableExtra("title");

        //定义listener
        listener = new View.OnClickListener() {
            Intent intent;
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.checkInButton:
                        intent = new Intent(CourseScoreDetail.this,CourseCheckInScore.class);
                        break;
                    case R.id.homeworkButton:
                        intent = new Intent(CourseScoreDetail.this,CourseHomeworkScore.class);
                        break;
                    case R.id.programButton:
                        intent = new Intent(CourseScoreDetail.this,CourseProgramScore.class);
                        break;
                    case R.id.totalScoreButton:
                        intent = new Intent(CourseScoreDetail.this,CourseTotalScore.class);
                        break;
                }
                intent.putExtra("course_id",id);
                intent.putExtra("course_title",title);
                intent.putExtra("background",imageName);
                startActivity(intent);
            }
        };

        //添加点击事件监听器
        checkInButton.setOnClickListener(listener);
        homeworkButton.setOnClickListener(listener);
        programButton.setOnClickListener(listener);
        totalScoreButton.setOnClickListener(listener);

        //设置标题样式
        setSupportActionBar(bar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bar.setTitle("当前课程");

        //初始化界面
        initial();
    }

    /**
     * @author Xie Jiadi
     * @time 2021/7/3 11:38
     * @description 初始化界面，主要是设置Toolbar的样式
     * @param
     */
    public void initial(){
        int imageID = getResources().getIdentifier(imageName,"drawable",getPackageName());
        toolbarBackground.setImageResource(imageID);
        toolbarCourseTitle.setText(title);
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
}
