package com.example.newwwdle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Information extends AppCompatActivity {

    Button cancel , post;
    EditText title , message;

    //DATE
    TextView DATE_view;
    String DATE;
    int isPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        cancel = findViewById(R.id.cacel_post);
        post = findViewById(R.id.post_buton);
        title = findViewById(R.id.info_Title);
        message = findViewById(R.id.info_text);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Information.this.finish();
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //發布訊息
                isPost = 1;
                Intent intent = new Intent();
                intent.setClass(Information.this, Teacher_class.class);
                Bundle bundle = new Bundle();
                bundle.putInt("flag", isPost);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        /**********************************
         * 把訊息跟tiltle裝進~~~~
         */

        DATE_view = (TextView)findViewById(R.id.date);
        SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dff.setTimeZone(TimeZone.getTimeZone("Asia/Taipei"));
        DATE = dff.format(new Date());
        if(DATE != null){
            DATE_view.setText("DATE : " + DATE.substring(5,10));
        }
    }
}
