package com.example.ordinaryscoreapp.Score;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.ordinaryscoreapp.R;

public class CourseScoreDetail extends AppCompatActivity {


    ImageView toolbarBackground;
    TextView toolbarCourseTitle;
    Toolbar bar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_score_detail);

        bar = this.findViewById(R.id.ScoreDetailToolbar);
        toolbarBackground = this.findViewById(R.id.ScoreDetailToolbarBackgroundImgView);
        toolbarCourseTitle = this.findViewById(R.id.CourseScoreToolbarCourseTitle);
        setSupportActionBar(bar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bar.setTitle("当前课程");

        initial();
    }

    public void initial(){
        String imageName = (String)getIntent().getSerializableExtra("background");
        int imageID = getResources().getIdentifier(imageName,"drawable",getPackageName());
        toolbarBackground.setImageResource(imageID);
        toolbarCourseTitle.setText((String)getIntent().getSerializableExtra("title"));
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
