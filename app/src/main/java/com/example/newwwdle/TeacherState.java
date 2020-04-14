package com.example.newwwdle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.CheckBox;

public class TeacherState extends AppCompatActivity {
    private String s1[]; //s1用來設定點名日期
    private String s2[]; //s2用來儲存當日點名狀態
    ;
    RecyclerView myTRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_state);

        myTRecyclerView = findViewById(R.id.stateRecyclerView2);

        //決定s1跟s2的來源
        s1 = getResources().getStringArray(R.array.student);
        s2 = getResources().getStringArray(R.array.state);

        //設定adapter
        Teacher_State_Adapter stateAdapter = new Teacher_State_Adapter(this, s1, s2);
        myTRecyclerView.setAdapter(stateAdapter);
        myTRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
