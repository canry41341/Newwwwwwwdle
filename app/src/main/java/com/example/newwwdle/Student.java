package com.example.newwwdle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class Student extends AppCompatActivity {

    private TextView nameTxt;   // show student name
    private TextView idText;    // show student id

    //this class 用來呈現課程選單
    //這個class用的adapter是MyAdapter
    private String id, name;

    private Button logout_btn; //press to log out

    //s1[], s2[]可以用來存取從database抓下來的資料
    String s1[], s2[], s3[];

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
        idText = findViewById(R.id.idView);

        //bundle可以在不同activity間傳遞參數
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        id =  bundle.getString("id");
        name = bundle.getString("name");
        idText.setText(id);
        nameTxt.setText(name);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        //這裡是看你要從哪去抓 s1[]跟s2[]的資料來源
        /******************課表、教室、時間(把時間跟教室放在s2)**************/
        s1 = bundle.getStringArray("s1");//getResources().getStringArray(R.array.class_Name);
        s2 = bundle.getStringArray("s2");//getResources().getStringArray(R.array.time);
        s3 = bundle.getStringArray("s3");//CID
        /*****************************************************************/
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
                        // Get token
                        FirebaseInstanceId.getInstance().getInstanceId()
                                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                        if (!task.isSuccessful()) {
                                            Log.w("TokenError", "getInstanceId failed", task.getException());
                                            return;
                                        }

                                        // Get new Instance ID token
                                        String token = task.getResult().getToken();
                                        SharedPreferences pref = getSharedPreferences("userdata", MODE_PRIVATE);
                                        pref.edit().putString("token", token).commit();
                                        // Toast.makeText(MainActivity.this, "TOKEN = "+msg, Toast.LENGTH_SHORT).show();
                                    }
                                });
                        // 加入黑名單
                        Backend backend = new Backend();
                        SharedPreferences pref = getSharedPreferences("userdata", MODE_PRIVATE);
                        backend.Communication(5, pref.getString("ID", ""), pref.getString("token", null));

                        // Clean the Databases
                        Student.this.deleteDatabase("ClassInfo.db");
                        pref.edit().clear();

                        // Set login flag to false
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
                    }
                });
                approve.show();
            }
        });

        //設定adapter(課表選單)
        MyAdapter myAdapter = new MyAdapter(this, s1,s2,s3);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
