package com.example.ordinaryscoreapp.Widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ordinaryscoreapp.R;

import java.util.ArrayList;
import java.util.List;

public class CourseStudentListViewAdapter extends ArrayAdapter {

    private ArrayList<String> items;
    private ArrayList<Boolean> checked;
    private LayoutInflater inflater;
    private TextView textView;

    public CourseStudentListViewAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        inflater = LayoutInflater.from(context);
        items = (ArrayList<String>) objects;
        checked = new ArrayList<Boolean>();
        for(int i=0;i<items.size();i++)
            checked.add(i,false);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = inflater.inflate(R.layout.widget_course_student_list, null);
            textView = convertView.findViewById(R.id.itemTextView);
        }
        textView.setText(items.get(position));
        textView.setOnClickListener(v -> {
            checked.set(position,!checked.get(position));
            if(checked.get(position)){
                textView.setBackgroundColor(0xFF00873C);
                textView.setTextColor(0xFFFFFFFF);
            }
            else{
                textView.setBackgroundColor(0xFFFFFFFF);
                textView.setTextColor(0xFF757575);
            }
        });
        return convertView;
    }
}
