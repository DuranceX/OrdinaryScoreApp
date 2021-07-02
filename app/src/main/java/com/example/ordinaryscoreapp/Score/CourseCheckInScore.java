package com.example.ordinaryscoreapp.Score;

import android.graphics.fonts.FontStyle;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.column.ColumnInfo;
import com.bin.david.form.data.table.MapTableData;
import com.bin.david.form.data.table.TableData;
import com.bin.david.form.listener.OnColumnClickListener;
import com.bin.david.form.listener.OnColumnItemClickListener;
import com.example.ordinaryscoreapp.DBUtil.CheckInScoreDAL;
import com.example.ordinaryscoreapp.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CourseCheckInScore extends AppCompatActivity {

    SmartTable table;
    String id;
    String imageName;
    String title;
    CheckInScoreDAL checkInScoreDAL;
    List tableDataList;
    ArrayList<Map<String,Object>> concreteTableDataList;
    MapTableData tableData;
    String[][] data;

    ImageView toolbarBackground;
    Toolbar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_checkin_score);

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
        bar.setSubtitle(title);

        //初始化表格数据
//        initTableData(id);
//        showTableData();
        checkInScoreDAL.dbInitial();
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


    public void initTableData(String id){
        String where = "course_id='" + id + "'";
        concreteTableDataList = checkInScoreDAL.dbFind(where);
        tableDataList = concreteTableDataList;
        data = new String[tableDataList.size()][concreteTableDataList.get(0).size()];
        for(int i =0;i<data.length;i++){
            data[i][0] = concreteTableDataList.get(i).get("course_id").toString();
            data[i][1] = concreteTableDataList.get(i).get("student_no").toString();
            data[i][2] = concreteTableDataList.get(i).get("student_name").toString();
            for (int j=3;j<data[0].length;j++){
                data[i][j] = concreteTableDataList.get(i).get("checkin_no_"+(j-2)).toString();
            }
        }
    }

    public void showTableData(){
        tableData = MapTableData.create(title + "考勤分表",tableDataList);
        tableData.setOnItemClickListener((column, value, o, col, row) -> {
            if(value.equals("1")){
                data[row][col] = "0";
            }
            else
                data[row][col] = "1";
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
            showTableData();
        });
        table.setTableData(tableData);
    }
}
