package com.example.newwwdle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class teacher extends AppCompatActivity {

    private teacher teacher;
    private String id, name;
    private RecyclerView recycler_view;
    private Teacher_Adapter adapter;
    private ArrayList<String> mData = new ArrayList<>();
    TextView student;
    Button out;

/******************************data是課程名稱，time就是時間+教室*******************/
    public String[] data;
    public String[] time;
    public String[] CID;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getApplicationInfo().targetSdkVersion >= Build.VERSION_CODES.ECLAIR) {
                event.startTracking();
            } else {
                onBackPressed(); // 是其他按鍵則再Call Back方法
            }
        }
        return false;
    }
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
        student = (TextView) findViewById(R.id.student_name);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        data = bundle.getStringArray("s1");
        time = bundle.getStringArray("s2");
        CID = bundle.getStringArray("s3");
        id = bundle.getString("id");
        name = bundle.getString("name");

/***************************抓老師資料再把"老師姓名改掉"  ********************************/
        student.setText(name+"\n"+id);

            // 準備資料，塞50個項目到ArrayList裡
        for (int i = 0; i < data.length; i++) {
            mData.add(data[i]);
        }

            // 連結元件
            recycler_view = (RecyclerView) findViewById(R.id.teacher_recyclerView);
            // 設置RecyclerView為列表型態
            recycler_view.setLayoutManager(new LinearLayoutManager(this));
            // 設置格線
            recycler_view.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

            // 將資料交給adapter
            adapter = new Teacher_Adapter(this,mData,time,CID);
            // 設置adapter給recycler_view
            recycler_view.setAdapter(adapter);
            out = findViewById(R.id.out_btn);
            out.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder approve = new AlertDialog.Builder(teacher.this);
                    approve.setTitle("確定登出?");
                    approve.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Log.d(TAG, "onClick: " + numberPicker.getValue());
                            // Set login flag to false
                            SharedPreferences pref = getSharedPreferences("userdata", MODE_PRIVATE);

                            // Clean the Databases
                            teacher.this.deleteDatabase("ClassInfo.db");
                            pref.edit().clear();

                            pref.edit().putBoolean("login_flag", false).commit();
                            // Go back to Login Window (MainActivity)
                            Intent intent = new Intent();
                            intent.setClass(teacher.this, MainActivity.class);;
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    });

                    approve.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Log.d(TAG, "onClick: " + numberPicker.getValue());
                        }
                    });
                    approve.show();

                }
            });
        }

}





