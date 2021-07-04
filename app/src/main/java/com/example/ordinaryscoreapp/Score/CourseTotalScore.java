package com.example.ordinaryscoreapp.Score;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.table.MapTableData;
import com.bin.david.form.data.table.TableData;
import com.example.ordinaryscoreapp.DBUtil.CourseScoreDAL;
import com.example.ordinaryscoreapp.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * @author Xie Jiadi
 * @time 2021/7/3 11:32
 * @description 显示总分分数页面
 */
public class CourseTotalScore extends AppCompatActivity {
    SmartTable table;
    String id;
    String imageName;
    String title;
    CourseScoreDAL courseScoreDAL;
    List tableDataList;
    ArrayList<Map<String,Object>> concreteTableDataList;
    TextView tableTitle;
    TextView addColumn;
    TextView delColumn;
    Button saveButton;
    MapTableData tableData;

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
        addColumn = this.findViewById(R.id.AddColumnButton);
        delColumn = this.findViewById(R.id.DelColumnButton);
        tableTitle = this.findViewById(R.id.TableTitle);
        saveButton = this.findViewById(R.id.SaveToEmailButton);

        //初始化变量
        id = (String) getIntent().getSerializableExtra("course_id");
        imageName = (String)getIntent().getSerializableExtra("background");
        title = (String)getIntent().getSerializableExtra("course_title");
        courseScoreDAL = new CourseScoreDAL(this);
        tableTitle.setText("总分表");
        addColumn.setVisibility(View.INVISIBLE);
        delColumn.setVisibility(View.INVISIBLE);

        //初始化标题栏样式
        setSupportActionBar(bar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        int imageID = getResources().getIdentifier(imageName,"drawable",getPackageName());
        toolbarBackground.setImageResource(imageID);
        bar.setTitle("录入上机分");
        bar.setSubtitle(title);

        //初始化表格数据
        initTableData(id);
        //showTableData();
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
     * @time 2021/7/3 11:32
     * @description 根据课程编号查询课程成绩表，初始化分数列表
     * @param id 课程编号
     */
    public void initTableData(String id){
        String where = "course_id='" + id + "'";
        concreteTableDataList = courseScoreDAL.dbFind(where);
        tableDataList = concreteTableDataList;

        //设置表格样式
        FontStyle fontStyle = new FontStyle();
        fontStyle.setTextSize(50);
        table.getConfig().setContentStyle(fontStyle);
        table.setZoom(true,Float.parseFloat("2.5"),Float.parseFloat("0.5"));
        table.getConfig().setFixedTitle(true);
        table.getConfig().setColumnTitleStyle(fontStyle);
        table.getConfig().setTableTitleStyle(fontStyle);
        table.getConfig().setShowXSequence(false);
        table.getConfig().setShowTableTitle(false);

        //绑定数据
        tableData = MapTableData.create(title + "上机分表",tableDataList);
        table.setTableData(tableData);
    }
}
