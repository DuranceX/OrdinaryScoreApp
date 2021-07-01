package com.example.ordinaryscoreapp.Widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ordinaryscoreapp.Course.CourseModify;
import com.example.ordinaryscoreapp.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Xie Jiadi
 * @time 2021/6/29 22:50
 * @description 自定义列表的Adapter类
 */
public class CourseListViewAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<Map<String,Object>> list;
    private final int CardType = 0;
    private final int ButtonType = 1;
    Context context;
    private final class ViewHolder{
        public ImageView background;
        public TextView courseTitle;
        public TextView courseLocation;
        public TextView courseTime;
        public FrameLayout item;
     }

    public CourseListViewAdapter(Context context, List<Map<String, Object>> list) {
        inflater = LayoutInflater.from(context);
        this.list = list;

        //数组末尾添加一个数据用来充当按钮的占位符
        HashMap button = new HashMap<String,Object>();
        button.put("button","button");
        list.add(button);

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
        int type = getItemViewType(position);
        if(convertView == null){
            if(type == CardType) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.widget_course_list_item, null);
                holder.background = (ImageView) convertView.findViewById(R.id.Course_item_background);
                holder.courseTitle = (TextView) convertView.findViewById(R.id.course_title);
                holder.courseLocation = (TextView) convertView.findViewById(R.id.course_location);
                holder.courseTime = (TextView) convertView.findViewById(R.id.course_time);
                holder.item = (FrameLayout) convertView.findViewById(R.id.FrameLayout_item);
                convertView.setTag(holder);
            }
            else{
                convertView = inflater.inflate(R.layout.widget_course_list_item_button,null);
                Button button = (Button) convertView.findViewById(R.id.button);
                button.setOnClickListener(v -> {
                    Intent intent = new Intent(context, CourseModify.class);
                    ((Activity) context).startActivityForResult(intent,0);
                });
            }
        }
        else{
            holder = (ViewHolder)convertView.getTag();
        }
        if(type == CardType){
            //设置背景
            String imgName = "img_courselistitem_background" + (position%5+1);
            holder.background.setBackgroundResource(getResourceId(imgName));

            //设置文本信息
            holder.courseTitle.setText((CharSequence)list.get(position).get("title"));
            holder.courseLocation.setText((CharSequence)list.get(position).get("location"));
            holder.courseTime.setText((CharSequence)list.get(position).get("time"));

            //设置点击事件
            ViewHolder temp = holder;
            holder.item.setOnClickListener(v -> {
                Intent intent = new Intent(context, CourseModify.class);
                intent.putExtra("id",(CharSequence)list.get(position).get("id"));
                intent.putExtra("background",imgName);
                ((Activity) context).startActivityForResult(intent,1);
            });
        }
        return convertView;
    }


    /**
     * @author Xie Jiadi
     * @time 2021/6/29 22:51
     * @description 判断是否是最后一个位置，是的话将样式改为按钮样式
     * @param position 位置索引
     */
    @Override
    public int getItemViewType(int position) {
        if(position == list.size()-1)
            return ButtonType;
        else
            return CardType;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
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
