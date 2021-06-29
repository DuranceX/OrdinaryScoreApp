package com.example.ordinaryscoreapp.Widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ordinaryscoreapp.R;

import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class CourseListViewAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<Map<String,Object>> list;
    Context context;
    private final class ViewHolder{
        public ImageView background;
        public TextView courseTitle;
        public TextView courseLocation;
        public TextView courseTime;
     }

    public CourseListViewAdapter(Context context, List<Map<String, Object>> list) {
        inflater = LayoutInflater.from(context);
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.course_list_item,null);
            holder.background = (ImageView)convertView.findViewById(R.id.Course_item_background);
            holder.courseTitle = (TextView)convertView.findViewById(R.id.course_title);
            holder.courseLocation = (TextView)convertView.findViewById(R.id.course_location);
            holder.courseTime = (TextView)convertView.findViewById(R.id.course_time);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder)convertView.getTag();
        }
        //设置背景
        String imgName = "img_courselistitem_background" + (position%5+1);
        holder.background.setBackgroundResource(getResourceId(imgName));

        //设置文本信息
        holder.courseTitle.setText((CharSequence)list.get(position).get("title"));
        holder.courseLocation.setText((CharSequence)list.get(position).get("location"));
        holder.courseTime.setText((CharSequence)list.get(position).get("time"));
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount();
    }

    /**
     * 根据图片名称获取资源ID
     * @param imageName
     * @return
     */
    public int getResourceId(String imageName){
        return context.getResources().getIdentifier(imageName,"drawable",context.getPackageName());
    }
}
