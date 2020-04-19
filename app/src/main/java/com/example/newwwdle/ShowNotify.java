package com.example.newwwdle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class ShowNotify extends AppCompatActivity {

    TextView Title , message , time;
    String title , date, msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        title =  bundle.getString("title");
        date = bundle.getString("time");
        msg = bundle.getString("msg");

        setContentView(R.layout.activity_show_notify);
        Title = findViewById(R.id.notify_title);
        message = findViewById(R.id.notify_message);
        time = findViewById(R.id.notify_date);

        message.setMovementMethod(ScrollingMovementMethod.getInstance());

        /***************抓課程資料**************/
        Title.setText(title);
        message.setText(msg);
        time.setText(date);
        /************************************/
    }
}
