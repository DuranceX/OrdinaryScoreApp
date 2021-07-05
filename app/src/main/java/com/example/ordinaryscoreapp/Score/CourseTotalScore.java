package com.example.ordinaryscoreapp.Score;

import android.Manifest;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.table.MapTableData;
import com.example.ordinaryscoreapp.DBUtil.CourseScoreDAL;
import com.example.ordinaryscoreapp.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
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
        saveButton.setOnClickListener(v -> {
            saveData();
        });

        //初始化标题栏样式
        setSupportActionBar(bar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        int imageID = getResources().getIdentifier(imageName,"drawable",getPackageName());
        toolbarBackground.setImageResource(imageID);
        bar.setTitle("查看成绩单");
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


    /**
     * @author Xie Jiadi
     * @time 2021/7/5 14:33
     * @description 保存数据，将读取到的数据保存为csv格式
     */
    public void saveData(){
        StringBuffer buffer = new StringBuffer();
        String excelTitle="";
        //使用迭代器获取Key值,添加标题行
        Iterator<String> iter = concreteTableDataList.get(0).keySet().iterator();
        while(iter.hasNext()){
            String temp = iter.next();
            excelTitle += temp;
            if(iter.hasNext())
               excelTitle +=  ",";
        }
//        excelTitle.replace(excelTitle.charAt(excelTitle.length()-1)+"","");
        excelTitle += "\r\n";
        buffer.append(excelTitle);

        //添加数据
        for(int i=0;i<concreteTableDataList.size();i++){
            iter = concreteTableDataList.get(i).keySet().iterator();
            String dataLine = "";
            while(iter.hasNext()){
                String key = iter.next();
                dataLine += concreteTableDataList.get(i).get(key).toString();
                if(iter.hasNext())
                    dataLine += ",";
            }
//            dataLine.replace(dataLine.charAt(dataLine.length()-1)+"","");
            dataLine += "\r\n";
            buffer.append(dataLine);
        }

        String data = buffer.toString();
        String filename = title + "成绩单.csv";
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        String path = cw.getExternalFilesDir("").toString();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
        if(!new File(path).exists()){
            new File(path).mkdirs();
        }

        File file = new File(path,filename);
        try {
            if(!file.exists())
                file.createNewFile();
            OutputStream out = new FileOutputStream(file);
            byte b[] = {(byte)0xEF, (byte)0xBB, (byte)0xBF};
            out.write(b);
            out.write(data.getBytes());
            out.close();
            Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        sendEmail(path + File.separator + filename);
    }


    /**
     * @author Xie Jiadi
     * @time 2021/7/5 14:33
     * @description 唤起邮箱应用发生邮件
     * @param fileName 待发送的文件名
     */
    public void sendEmail(String fileName){
        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        String email = null;
        View dialogBackground = LayoutInflater.from(this).inflate(R.layout.widget_email_inputdialog, null);
        EditText nameEditText = dialogBackground.findViewById(R.id.EmailEditText);
        TextView emailLabel = dialogBackground.findViewById(R.id.EmailInputLabel);
        if(sp.getString("email",email)!=null){
            email = sp.getString("email",email);
        }
        nameEditText.setText(email);
        emailLabel.setText("请输入接收邮箱");
        new SweetAlertDialog(this,SweetAlertDialog.NORMAL_TYPE)
                .setCustomView(dialogBackground)
                .setConfirmText("ok")
                .setConfirmClickListener(v -> {
                    //写入sharedPreferences
                    editor.putString("email",nameEditText.getText().toString());
                    editor.commit();

                    Intent intent = new Intent(Intent.ACTION_SEND);
                    String[] tos = {nameEditText.getText().toString()};
                    intent.putExtra(Intent.EXTRA_EMAIL,tos);
                    intent.putExtra(Intent.EXTRA_TEXT,"成绩单");
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("content://" + fileName));
                    intent.setType("file/*");
                    intent.setType("message/rfc882");
                    Intent.createChooser(intent, "Choose Email Client");
                    v.dismiss();
                    startActivity(intent);
                })
                .show();
    }
}
