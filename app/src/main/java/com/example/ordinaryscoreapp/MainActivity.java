package com.example.ordinaryscoreapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ordinaryscoreapp.Course.CourseOverview;
import com.example.ordinaryscoreapp.Score.CourseScoreOverview;
import com.example.ordinaryscoreapp.Student.StudentOverview;

public class MainActivity extends AppCompatActivity {
    private ImageView CourseButton;
    private ImageView StudentButton;
    private ImageView ScoreButton;
    private View.OnClickListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CourseButton = this.findViewById(R.id.CourseButton);
        StudentButton = this.findViewById(R.id.StudentButton);
        ScoreButton = this.findViewById(R.id.ScoreButton);

        listener = v -> {
            Intent intent;
            switch (v.getId())
            {
                case R.id.CourseButton:
                    intent = new Intent(MainActivity.this, CourseOverview.class);
                    startActivity(intent);
                    break;
                case R.id.StudentButton:
                    intent = new Intent(MainActivity.this, StudentOverview.class);
                    startActivity(intent);
                    break;
                case R.id.ScoreButton:
                    intent = new Intent(MainActivity.this, CourseScoreOverview.class);
                    startActivity(intent);
                    break;
            }
        };

        CourseButton.setOnClickListener(listener);
        StudentButton.setOnClickListener(listener);
        ScoreButton.setOnClickListener(listener);

    }
}
