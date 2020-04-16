package com.example.newwwdle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Student extends AppCompatActivity {

    private TextView nameTxt; // show student name

    //this class 用來呈現課程選單
    //這個class用的adapter是MyAdapter
    private String id;

    private Button logout_btn; //press to log out

    //s1[], s2[]可以用來存取從database抓下來的資料
    String s1[], s2[];

    RecyclerView recyclerView;
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getApplicationInfo().targetSdkVersion >= Build.VERSION_CODES.ECLAIR) {
                event.startTracking();
            } else {
                onBackPressed(); // 是其他按鍵則再Call Back方法
            }
        }
        return false;
    }
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return super.onKeyUp(keyCode, event);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        nameTxt = findViewById(R.id.nameView);

        //bundle可以在不同activity間傳遞參數
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        id =  bundle.getString("name");
        nameTxt.setText(id);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        //這裡是看你要從哪去抓 s1[]跟s2[]的資料來源
        s1 = getResources().getStringArray(R.array.class_Name);
        s2 = getResources().getStringArray(R.array.time);

        //set log out
        logout_btn = findViewById(R.id.logout_btn);
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder approve = new AlertDialog.Builder(Student.this);
                approve.setTitle("確定登出?");
                approve.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Log.d(TAG, "onClick: " +
                        // Set login flag to false
                        SharedPreferences pref = getSharedPreferences("userdata", MODE_PRIVATE);
                        pref.edit().putBoolean("login_flag", false).commit();
                        // Go back to Login Window (MainActivity)
                        Intent intent = new Intent();
                        intent.setClass(Student.this, MainActivity.class);;
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });

                approve.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Log.d(TAG, "onClick: " + numberPicker.getValue());
                    }
                });
                approve.show();
            }
        });

        //設定adapter
        MyAdapter myAdapter = new MyAdapter(this, s1,s2);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
