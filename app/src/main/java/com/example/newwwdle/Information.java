package com.example.newwwdle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
    InputMethodManager imm ;
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
        message.setMovementMethod(ScrollingMovementMethod.getInstance());

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
                /***********************傳到databse*********************/
                title.getText();
                message.getText();
                /********************************************************/
                Information.this.finish();
            }
        });

        DATE_view = (TextView)findViewById(R.id.date);
        SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dff.setTimeZone(TimeZone.getTimeZone("Asia/Taipei"));
        DATE = dff.format(new Date());
        if(DATE != null){
            DATE_view.setText("DATE : " + DATE.substring(5,10));
        }
    }
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            //隱藏鍵盤
            imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(Information.this.getCurrentFocus().getWindowToken(), 0);
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //輸入框當前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
                // 典籍的是輸入框，並不包括edittext的範圍
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 保留touchevent
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }
}
