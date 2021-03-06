package com.example.newwwdle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class TeacherCheck extends AppCompatActivity {

    RecyclerView mRecyclerview;
    Backend backend = new Backend();
    static Activity act;


    //把從server抓到到的日期放到這裡
    String[] date;
    String[][] student;
    String CID;

    int temp = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_check);
        act = this;
        Teacher_class.attend_all_btn.setEnabled(true);
        //CID
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        CID =  bundle.getString("CID");
        //

        //
        String result = backend.Communication(3,CID);
        System.out.println("aaa: " + result);
        String[] tokens = result.split(";");
        for(int i = 0; i < tokens.length; i++){
            String[] ID_split = tokens[i].split(":");
            String[] day_split = ID_split[1].split(",");
            if(temp == 0){
                student = new String[day_split.length+1][tokens.length+1];
                date = new String[day_split.length];
                temp += 1;
            }
            student[0][i+1] = ID_split[0];
            for(int j = 0; j < day_split.length; j++){
                System.out.println(day_split[j]);
                String[] check_split = day_split[j].split("/");
                student[j+1][0] = check_split[0].substring(5);
                date[j] = check_split[0].substring(5);
                student[j+1][i+1] = check_split[1];
            }

        }

        mRecyclerview = findViewById(R.id.grid_view);

        CheckAdapter checkAdapter = new CheckAdapter(this, date, student, CID);
        mRecyclerview.setAdapter(checkAdapter);
        mRecyclerview.setLayoutManager(new GridLayoutManager(this, 2));
    }

}
