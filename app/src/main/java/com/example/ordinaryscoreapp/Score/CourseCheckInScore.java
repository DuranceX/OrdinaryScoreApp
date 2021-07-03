package com.example.ordinaryscoreapp.Score;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.table.MapTableData;
import com.example.ordinaryscoreapp.DBUtil.CheckInScoreDAL;
import com.example.ordinaryscoreapp.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Xie Jiadi
 * @time 2021/7/3 11:29
 * @description 考勤分数页面
 */
public class CourseCheckInScore extends AppCompatActivity {

    SmartTable table;
    String id;
    String imageName;
    String title;
    CheckInScoreDAL checkInScoreDAL;
    List tableDataList;
    ArrayList<Map<String,Object>> concreteTableDataList;
    MapTableData tableData;
    //用二维数组记录UI表中的值，便于操作
    String[][] data;

    ImageView toolbarBackground;
    Toolbar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_score);

        //绑定组件
        bar = this.findViewById(R.id.CheckInScoreToolbar);
        toolbarBackground = this.findViewById(R.id.CheckInScoreToolbarBackgroundImgView);
        table = (SmartTable)this.findViewById(R.id.checkInScoreTable);

        //初始化变量
        id = (String) getIntent().getSerializableExtra("course_id");
        imageName = (String)getIntent().getSerializableExtra("background");
        title = (String)getIntent().getSerializableExtra("course_title");
        checkInScoreDAL = new CheckInScoreDAL(this);

        //初始化标题栏样式
        setSupportActionBar(bar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        int imageID = getResources().getIdentifier(imageName,"drawable",getPackageName());
        toolbarBackground.setImageResource(imageID);
        bar.setTitle("录入考勤分");
        bar.setSubtitle(title);

        //初始化表格数据
        initTableData(id);
        //checkInScoreDAL.dbInitial();
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
     * @time 2021/7/3 11:26
     * @description 根据课程编号查询课程成绩表，初始化考勤分数列表
     * @param id 课程编号
     */
    public void initTableData(String id){
        String where = "course_id='" + id + "'";
        concreteTableDataList = checkInScoreDAL.dbFind(where);
        tableDataList = concreteTableDataList;

        //将查询结果写入data数组中
        data = new String[tableDataList.size()][concreteTableDataList.get(0).size()];
        for(int i =0;i<data.length;i++){
            data[i][0] = concreteTableDataList.get(i).get("course_id").toString();
            data[i][1] = concreteTableDataList.get(i).get("student_no").toString();
            data[i][2] = concreteTableDataList.get(i).get("student_name").toString();
            for (int j=3;j<data[0].length;j++){
                data[i][j] = concreteTableDataList.get(i).get("checkin_no_"+(j-2)).toString();
            }
        }

        //设置表格样式
        FontStyle fontStyle = new FontStyle();
        fontStyle.setTextSize(50);
        table.getConfig().setContentStyle(fontStyle);
        table.setZoom(true,Float.parseFloat("2.5"),Float.parseFloat("0.5"));
        table.getConfig().setFixedTitle(true);
        table.getConfig().setColumnTitleStyle(fontStyle);
        table.getConfig().setTableTitleStyle(fontStyle);
        table.getConfig().setShowXSequence(false);

        //绑定数据
        tableData = MapTableData.create(title+"考勤分表",tableDataList);
        addListener();
        table.setTableData(tableData);
    }

    //弃用
    public void showTableData(){
        tableData = MapTableData.create(title + "考勤分表",tableDataList);
        table.setTableData(tableData);
    }


    /**
     * @author Xie Jiadi
     * @time 2021/7/3 11:28
     * @description 为表格添加点击事件
     */
    public void addListener(){
        tableData.setOnItemClickListener((column, value, o, col, row) -> {
            if(value.equals("√")){
                data[row][col] = "";
            }
            else if(value.equals(""))
                data[row][col] = "√";
            concreteTableDataList.clear();
            for(int i = 0; i <data.length; i++){
                Map<String,Object> temp = new LinkedHashMap<>();
                temp.put("course_id",data[i][0]);
                temp.put("student_no",data[i][1]);
                temp.put("student_name",data[i][2]);
                for(int j=3;j<data[0].length;j++){
                    temp.put("checkin_no_" + (j-2),data[i][j]);
                }
                concreteTableDataList.add(temp);
            }
            tableDataList=concreteTableDataList;
            tableData = MapTableData.create(title + "考勤分表",tableDataList);
            table.notifyDataChanged();
        });
    }


    /**
     * @author Xie Jiadi
     * @time 2021/7/3 11:28
     * @description 退出页面时保存数据到数据库
     */
    @Override
    protected void onStop() {
        super.onStop();
        checkInScoreDAL.dbUpdate(concreteTableDataList);
    }
}
