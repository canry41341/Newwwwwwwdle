package com.example.newwwdle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.Toast;

public class TeacherState extends AppCompatActivity {
    private String s1[]; //s1用來設定點名日期
    private String s2[]; //s2用來儲存當日點名狀態
    String date;
    RecyclerView myTRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_state);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        date =  bundle.getString("date1");
        Toast.makeText(TeacherState.this, date, Toast.LENGTH_LONG).show();


        myTRecyclerView = findViewById(R.id.stateRecyclerView2);

        //決定s1跟s2的來源
        /*************************抓資料**************************************/
        s1 = getResources().getStringArray(R.array.student);
        s2 = getResources().getStringArray(R.array.state);
        /************************************************************************/
        //設定adapter
        Teacher_State_Adapter stateAdapter = new Teacher_State_Adapter(this, s1, s2);
        myTRecyclerView.setAdapter(stateAdapter);
        myTRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myTRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }
}
