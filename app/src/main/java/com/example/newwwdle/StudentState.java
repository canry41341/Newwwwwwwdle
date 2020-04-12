package com.example.newwwdle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class StudentState extends AppCompatActivity {

    //這個class用的adapter是StateAdapter
    private String s1[]; //s1用來設定點名日期
    private String s2[]; //s2用來儲存當日點名狀態

    RecyclerView myRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_state);

        myRecyclerView = findViewById(R.id.stateRecyclerView);

        //決定s1跟s2的來源
        s1 = getResources().getStringArray(R.array.date);
        s2 = getResources().getStringArray(R.array.state);

        StateAdapter stateAdapter = new StateAdapter(this, s1, s2);
        myRecyclerView.setAdapter(stateAdapter);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
