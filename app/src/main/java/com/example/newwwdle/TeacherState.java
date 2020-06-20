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
    private String s1[]; //SID
    private String s2[]; //check
    private String CID;
    String date;
    int today, choose;
    RecyclerView myTRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_state);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        date =  bundle.getString("date1");

        myTRecyclerView = findViewById(R.id.stateRecyclerView2);

        //決定s1跟s2的來源
        /*************************抓資料**************************************/
        s1 = bundle.getStringArray("student");
        s2 = bundle.getStringArray("check");
        CID = bundle.getString("CID");
        today = bundle.getInt("today");
        choose = bundle.getInt("choose");

        /************************************************************************/
        //設定adapter
        Teacher_State_Adapter stateAdapter = new Teacher_State_Adapter(this, s1, s2, CID, today, choose);
        myTRecyclerView.setAdapter(stateAdapter);
        myTRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myTRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }
}
