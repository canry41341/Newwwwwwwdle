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

    private String id;
    String s1[], s2[];
    RecyclerView recyclerView;

    private ArrayList<String> class_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        id = bundle.getString("name");

        //setInit();

        recyclerView = findViewById(R.id.recyclerView);

        s1 = getResources().getStringArray(R.array.class_Name);
        s2 = getResources().getStringArray(R.array.time);

        MyAdapter myAdapter = new MyAdapter(this, s1,s2);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /*private void setInit() {
        ListView classView = findViewById(R.id.listView);
        String [] class_Name = {"軟體工程", "體育課"};
        String [] teacher_name = {"chen", "lee"};
        String [] date = {"331", "404"};

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, class_Name);
        classView.setAdapter(arrayAdapter);


    }*/



}
