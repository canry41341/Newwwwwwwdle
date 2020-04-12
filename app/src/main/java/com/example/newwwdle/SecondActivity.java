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

    String data1, data2; //data1用來存去你點哪個課程名稱
                        //data2用來存你所點課程的上課時間

    String s1[], s2[]; //s1[]顯示課程名稱  s2[]顯示上課時間

    RecyclerView myRecyclerView;

    private Button atten_btn, notice_btn, state_btn; //分別是 "點名鈕"  "通知鈕"  "顯示點名狀態的按鈕"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        className = findViewById(R.id.className);
        time = findViewById(R.id.timeTxt);

        myRecyclerView = findViewById(R.id.notifyView);

        //後端自己決定要從哪抓s1[]跟s2[]來源
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
                //按通知鈕後才會顯示通知
                showNotify();
                Toast.makeText(SecondActivity.this, "click success!" , Toast.LENGTH_SHORT).show();
            }
        });

        //check點名狀態
        state_btn = findViewById(R.id.state_btn);
        state_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //進到下一個activity來檢視點名情況
                startActivity(new Intent(SecondActivity.this, StudentState.class));
            }
        });


        getData(); //接收你從上個activity傳來的參數
        setData(); //顯示你從上個activity傳來的參數
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
        //設定adapter
        NotifyAdapter notifyAdapter = new NotifyAdapter(this, s1, s2);
        myRecyclerView.setAdapter(notifyAdapter);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}
