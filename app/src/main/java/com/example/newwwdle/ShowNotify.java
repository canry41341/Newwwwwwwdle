package com.example.newwwdle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class ShowNotify extends AppCompatActivity {

    TextView Title , message , time;
    String classname , date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        classname =  bundle.getString("data1");
        date = bundle.getString("data2");
        
        setContentView(R.layout.activity_show_notify);
        Title = findViewById(R.id.notify_title);
        message = findViewById(R.id.notify_message);
        time = findViewById(R.id.notify_date);

        message.setMovementMethod(ScrollingMovementMethod.getInstance());

        /***************抓課程資料**************/
        //Title.setText();
        message.setText("軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到軟體工程deadline要到了");
        //time.setText();
        /************************************/
    }
}
