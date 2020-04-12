package com.example.newwwdle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Information extends AppCompatActivity {

    Button cancel , post;
    EditText title , message;
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
            }
        });
        /**********************************
         * 把訊息跟tiltle裝進~~~~
         */

    }
}
