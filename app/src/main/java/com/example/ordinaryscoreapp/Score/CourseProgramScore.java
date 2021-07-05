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
import com.example.ordinaryscoreapp.DBUtil.ProgramDAL;
import com.example.ordinaryscoreapp.Model.Constants;
import com.example.ordinaryscoreapp.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * @author Xie Jiadi
 * @time 2021/7/3 11:32
 * @description 上机分数页面，详细注释见CourseCheckInScore
 */
public class CourseProgramScore extends AppCompatActivity {
    SmartTable table;
    String COLUMN_NAME = "program";
    String id;
    String imageName;
    String title;
    ProgramDAL programDAL;
    CourseScoreDAL courseScoreDAL;
    List tableDataList;
    ArrayList<Map<String,Object>> concreteTableDataList;
    TextView tableTitle;
    TextView addColumn;
    TextView delColumn;
    Button saveButton;
    MapTableData tableData;
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
        addColumn = this.findViewById(R.id.AddColumnButton);
        delColumn = this.findViewById(R.id.DelColumnButton);
        tableTitle = this.findViewById(R.id.TableTitle);
        saveButton = this.findViewById(R.id.SaveToEmailButton);

        //初始化变量
        id = (String) getIntent().getSerializableExtra("course_id");
        imageName = (String)getIntent().getSerializableExtra("background");
        title = (String)getIntent().getSerializableExtra("course_title");
        programDAL = new ProgramDAL(this);
        courseScoreDAL = new CourseScoreDAL(this);
        tableTitle.setText("上机分表");
        saveButton.setVisibility(View.INVISIBLE);
        addColumn.setOnClickListener(v -> {
            Constants.ProgramColumnNumber++;
            String columnName = "program_no_" + Constants.ProgramColumnNumber;
            courseScoreDAL.addColumn(columnName);
            initTableData(id);
        });
        delColumn.setOnClickListener(v -> {
            String columnName = "program_no_" + Constants.ProgramColumnNumber;
            courseScoreDAL.delColumn(columnName,this);
            Constants.ProgramColumnNumber--;
            initTableData(id);
        });

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
     * @description 根据课程编号查询课程成绩表，初始化考勤分数列表
     * @param id 课程编号
     */
    public void initTableData(String id){
        //在删除列后是一个新的同名数据库，需要重新获取
        programDAL = new ProgramDAL(CourseProgramScore.this);
        String where = "course_id='" + id + "'";
        concreteTableDataList = programDAL.dbFind(where);
        tableDataList = concreteTableDataList;
        data = new String[tableDataList.size()][concreteTableDataList.get(0).size()];
        for(int i =0;i<data.length;i++){
            data[i][0] = concreteTableDataList.get(i).get("course_id").toString();
            data[i][1] = concreteTableDataList.get(i).get("student_no").toString();
            data[i][2] = concreteTableDataList.get(i).get("student_name").toString();
            for (int j=3;j<data[0].length;j++){
                data[i][j] = concreteTableDataList.get(i).get(COLUMN_NAME+"_no_"+(j-2)).toString();
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
        table.getConfig().setShowTableTitle(false);

        //绑定数据
        tableData = MapTableData.create(title + "上机分表",tableDataList);
        addListener();
        table.setTableData(tableData);
    }

    /**
     * @author Xie Jiadi
     * @time 2021/7/3 11:32
     * @description 为表格添加点击事件
     */
    public void addListener(){
        tableData.setOnItemClickListener(new TableData.OnItemClickListener() {
            @Override
            public void onClick(Column column, String value, Object o, int col, int row) {
                //dialogBackground的设置必须放在onClick方法内部，因为对话框中有setView的方法，所有如果放在外面，会出现”
                //The specified child already has a parent. You must call removeView()"报错
                //需要重新生成子视图，不然通过setCustomView方法，一个子视图将被指定多个父视图
                View dialogBackground = LayoutInflater.from(CourseProgramScore.this).inflate(R.layout.widget_course_score_inputdialog, null);
                TextView nameLabel = dialogBackground.findViewById(R.id.CourseScoreStudentNameLabel);
                EditText nameEditText = dialogBackground.findViewById(R.id.EmailEditText);
                nameLabel.setText(data[row][2]);
                nameEditText.setText(data[row][col]);
                new SweetAlertDialog(CourseProgramScore.this,SweetAlertDialog.NORMAL_TYPE)
                        .setCustomView(dialogBackground)
                        .setConfirmText("ok")
                        .setConfirmClickListener(v -> {
                            data[row][col] = nameEditText.getText().toString();
                            concreteTableDataList.clear();
                            for(int i = 0; i <data.length; i++){
                                Map<String,Object> temp = new LinkedHashMap<>();
                                temp.put("course_id",data[i][0]);
                                temp.put("student_no",data[i][1]);
                                temp.put("student_name",data[i][2]);
                                for(int j=3;j<data[0].length;j++){
                                    temp.put(COLUMN_NAME+"_no_" + (j-2),data[i][j]);
                                }
                                concreteTableDataList.add(temp);
                            }
                            tableDataList=concreteTableDataList;
                            table.notifyDataChanged();
                            v.dismiss();
                        })
                        .show();
            }
        });
    }


    /**
     * @author Xie Jiadi
     * @time 2021/7/3 11:33
     * @description 退出页面时保存数据到数据库
     * @param
     */
    @Override
    protected void onStop() {
        super.onStop();
        programDAL.dbUpdate(concreteTableDataList);
    }
}
