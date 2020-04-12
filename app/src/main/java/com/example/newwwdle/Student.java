package com.example.newwwdle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Student extends AppCompatActivity {

    //this class 用來呈現課程選單
    //這個class用的adapter是MyAdapter
    private String id;

    //s1[], s2[]可以用來存取從database抓下來的資料
    String s1[], s2[];

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        //bundle可以在不同activity間傳遞參數
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        id = bundle.getString("name");

        recyclerView = findViewById(R.id.recyclerView);

        //這裡是看你要從哪去抓 s1[]跟s2[]的資料來源
        s1 = getResources().getStringArray(R.array.class_Name);
        s2 = getResources().getStringArray(R.array.time);

        //設定adapter
        MyAdapter myAdapter = new MyAdapter(this, s1,s2);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
