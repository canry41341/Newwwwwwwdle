package com.example.newwwdle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    private String id;
    private RecyclerView recycler_view;
    private Teacher_Adapter adapter;
    private ArrayList<String> mData = new ArrayList<>();
    TextView student;
    Button out;


    public String[] data = {"軟體工程", "醫學與健康"};
    public String[] time = {"\n(二) 7-9 資訊系館4202","\n(五) 3-4 繁城講堂"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
        student = (TextView) findViewById(R.id.student_name);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        id = bundle.getString("name");

        student.setText("學生姓名"+"\n"+id);

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
            adapter = new Teacher_Adapter(this,mData,time);
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





