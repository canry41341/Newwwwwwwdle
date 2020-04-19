package com.example.newwwdle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

public class TeacherCheck extends AppCompatActivity {

    RecyclerView mRecyclerview;
    Backend backend = new Backend();


    //把從server抓到到的日期放到這裡
    //private String [] date = {"Apr1","Apr2","Apr3","Apr4","Apr5","Apr6","Apr7","Apr8","Apr9","Apr10"};
    String[] date;
    String[][] student;

    int temp = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_check);

        //
        String result = backend.Communication(3,"CID1");
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
        //System.out.println(date);






        mRecyclerview = findViewById(R.id.grid_view);

        CheckAdapter checkAdapter = new CheckAdapter(this, date, student);
        mRecyclerview.setAdapter(checkAdapter);
        mRecyclerview.setLayoutManager(new GridLayoutManager(this, 2));
    }
}
