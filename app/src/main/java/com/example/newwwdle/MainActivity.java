package com.example.newwwdle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText name, password;
    private Button login_btn;
    InputMethodManager imm ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.edit_name); //username(student ID)
        password = findViewById(R.id.edit_id);//password
        login_btn = findViewById(R.id.login_btn);

        // check if user is already log in
        final SharedPreferences pref = getSharedPreferences("userdata", MODE_PRIVATE);
        boolean login_flag = pref.getBoolean("login_flag", false);
        if(login_flag){
            String ID = pref.getString("ID", "Unknown");
            String type = pref.getString("type", "Unknown");
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            if(ID.equals("Unknown") | type.equals("Unknown")){
                pref.edit().putBoolean("login_flag", false).commit();   // Login error, set login_flag back to false
            }
            // Already log in as student, jump to student window
            else if (type.equals("student")){
                intent.setClass(MainActivity.this, Student.class);
                bundle.putString("name", ID);    //send ID to next activity
                intent.putExtras(bundle);
                startActivity(intent);
            }
            // Already log in as teacher, jump to teacher window
            else if (type.equals("teacher")){
                intent.setClass(MainActivity.this, teacher.class);
                bundle.putString("name", ID);   //send ID to next activity
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.getText().toString().equals("") | password.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this, "請輸入帳號密碼", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    switch (password.getText().toString()) {
                        case "s":
                            intent.setClass(MainActivity.this, Student.class);
                            bundle.putString("name", name.getText().toString());//send student ID to next activity
                            intent.putExtras(bundle);
                            startActivity(intent);
                            // Save ID and type, turn login_flag to true
                            pref.edit().putBoolean("login_flag", true)
                                    .putString("ID", name.getText().toString())
                                    .putString("type", "student").commit();
                            break;
                        case "t":
                            intent.setClass(MainActivity.this, teacher.class);
                            bundle.putString("name", name.getText().toString());//send student ID to next activity
                            intent.putExtras(bundle);
                            startActivity(intent);
                            // Save ID and type, turn login_flag to true
                            pref.edit().putBoolean("login_flag", true)
                                    .putString("ID", name.getText().toString())
                                    .putString("type", "teacher").commit();
                            break;
                    }
                }
            }
        });

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            //隱藏鍵盤
            imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(), 0);
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
