package com.example.newwwdle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewDebug;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

public class Teacher_class extends AppCompatActivity {

    private Button atttend_btn , info_btn , noti_btn;
    TextView className;
    String data1, data2;
    boolean start;
    String ss1[], ss2[];

    RecyclerView myTRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_class);
        start = false;
        final Drawable d = getResources().getDrawable(R.drawable.button);
        ss1 = getResources().getStringArray(R.array.class_Name);
        ss2 = getResources().getStringArray(R.array.time);

        className = findViewById(R.id.className1);
        atttend_btn = findViewById(R.id.attendence_btn1);

        atttend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //老師想要點名所以她按了這個按鈕
                    if(!start) {
                        atttend_btn.setText("結束點名");
                        atttend_btn.setBackgroundColor(Color.RED);
                        final AlertDialog.Builder d = new AlertDialog.Builder(Teacher_class.this);
                        LayoutInflater inflater = Teacher_class.this.getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.dialog, null);
                        d.setTitle("設定點名時間");
                        d.setView(dialogView);
                        final NumberPicker numberPicker = (NumberPicker) dialogView.findViewById(R.id.dialog_number_picker);
                        numberPicker.setMaxValue(30);
                        numberPicker.setMinValue(10);
                        numberPicker.setWrapSelectorWheel(false);
                        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                            @Override
                            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                                //Log.d(TAG, "onValueChange: ");
                                Toast.makeText(Teacher_class.this, String.valueOf(numberPicker.getValue()), Toast.LENGTH_SHORT).show();
                            }
                        });
                        d.setPositiveButton("開始點名", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Log.d(TAG, "onClick: " + numberPicker.getValue());
                                Toast.makeText(Teacher_class.this, "點名開始", Toast.LENGTH_SHORT).show();
                            }
                        });
                        AlertDialog alertDialog = d.create();
                        alertDialog.show();
                        start = true;
                    }else{
                        atttend_btn.setText("點名");
                        atttend_btn.setBackgroundDrawable(d);
                        Toast.makeText(Teacher_class.this, "停止點名", Toast.LENGTH_SHORT).show();
                        start = false;
                    }

            }
        });


        info_btn = findViewById(R.id.notification_btn1);
        info_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(Teacher_class.this, Information.class);;
                startActivity(intent);

            }
        });

        noti_btn = findViewById(R.id.status_btn1);// 狀態按鈕
        noti_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNotify();
                Toast.makeText(Teacher_class.this, "click success!" , Toast.LENGTH_SHORT).show();
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
    }

    private void showNotify() {
        NotifyAdapter TnotifyAdapter = new NotifyAdapter(this, ss1, ss2,1);
        myTRecyclerView.setAdapter(TnotifyAdapter);
        myTRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
