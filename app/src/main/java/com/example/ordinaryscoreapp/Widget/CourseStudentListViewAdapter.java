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

import com.example.ordinaryscoreapp.Model.Student;
import com.example.ordinaryscoreapp.R;

import java.util.ArrayList;
import java.util.List;

public class CourseStudentListViewAdapter extends ArrayAdapter {

    private ArrayList<String> items;
    private ArrayList<Boolean> checked;
    private LayoutInflater inflater;
    private final class ViewHolder{
        public TextView textView;
    }

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
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.widget_course_student_list, null);
            holder.textView = convertView.findViewById(R.id.itemTextView);
            convertView.setTag(holder);
        }
        else
            holder = (ViewHolder)convertView.getTag();

        holder.textView.setText(items.get(position));
        if(checked.get(position)){
            holder.textView.setBackgroundColor(0xFF00873C);
            holder.textView.setTextColor(0xFFFFFFFF);
        }
        else{
            holder.textView.setBackgroundColor(0xFFFFFFFF);
            holder.textView.setTextColor(0xFF757575);
        }
        return convertView;
    }

    /**
     * @author Xie Jiadi
     * @time 2021/7/1 13:48
     * @description 设置选中数组
     * @param i 点击的Item的position
     */
    public void setClickItem(int i){
        checked.set(i,!checked.get(i));
    }


    /**
     * @author Xie Jiadi
     * @time 2021/7/1 13:48
     * @description 返回选中情况数组
     * @return ArrayLise<Boolean>
     */
    public ArrayList<Boolean> getChecked(){
        return checked;
    }
}
