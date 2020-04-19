package com.example.newwwdle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class TeacherCheck extends AppCompatActivity {

    RecyclerView mRecyclerview;

    //把從server抓到到的日期放到這裡
    private String [] date = {"Apr1","Apr2","Apr3","Apr4","Apr5","Apr6","Apr7","Apr8","Apr9","Apr10"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_check);

        mRecyclerview = findViewById(R.id.grid_view);

        CheckAdapter checkAdapter = new CheckAdapter(this, date);
        mRecyclerview.setAdapter(checkAdapter);
        mRecyclerview.setLayoutManager(new GridLayoutManager(this, 2));
    }
}
