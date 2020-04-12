package com.example.newwwdle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {

    //點選完課程後進入到此class
    TextView className, time;
    String data1, data2;
    String s1[], s2[];

    RecyclerView myRecyclerView;

    private Button atten_btn, notice_btn, state_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        className = findViewById(R.id.className);
        time = findViewById(R.id.timeTxt);

        myRecyclerView = findViewById(R.id.notifyView);
        s1 = getResources().getStringArray(R.array.class_Name);
        s2 = getResources().getStringArray(R.array.time);


        //check 點名是否成功
        atten_btn = findViewById(R.id.attendence_btn);
        atten_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Toast.makeText(SecondActivity.this, "點名成功", Toast.LENGTH_SHORT).show();
                }catch (Exception e) {
                    Toast.makeText(SecondActivity.this, "無法點名", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //check通知
        notice_btn = findViewById(R.id.notification_btn);
        notice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNotify();
                Toast.makeText(SecondActivity.this, "click success!" , Toast.LENGTH_SHORT).show();
            }
        });

        //check點名狀態
        state_btn = findViewById(R.id.state_btn);
        state_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SecondActivity.this, StudentState.class));
            }
        });


        getData();
        setData();
    }

    private void getData() {
        if(getIntent().hasExtra("data1") && getIntent().hasExtra("data2")) {

            data1 = getIntent().getStringExtra("data1");
            data2 = getIntent().getStringExtra("data2");
        }else {
            Toast.makeText(this, "No data!", Toast.LENGTH_SHORT).show();
        }
    }

    private void setData() {
        className.setText(data1);
        time.setText(data2);
    }

    private void showNotify() {
        NotifyAdapter notifyAdapter = new NotifyAdapter(this, s1, s2);
        myRecyclerView.setAdapter(notifyAdapter);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /*private void setListener1() { notice_btn.setOnClickListener(bevent1);}
    private View.OnClickListener bevent1 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showNotify();
            Toast.makeText(SecondActivity.this, "click success!" , Toast.LENGTH_SHORT).show();
        }
    };

    private void setListener2() { state_btn.setOnClickListener(bevent2);}
    private View.OnClickListener bevent2 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(SecondActivity.this, StudentState.class));
        }
    };*/

}
