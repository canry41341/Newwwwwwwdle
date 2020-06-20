package com.example.newwwdle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Information extends AppCompatActivity {

    Button cancel , post;
    EditText title , message;
    //DATE
    TextView DATE_view;
    String result;
    InputMethodManager imm ;
    String DATE,TITLE,MSG,CID;
    int isPost;

    AlertDialog alertDialog;
    ProgressBar progressbar;
    Backend backend = new Backend();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        cancel = findViewById(R.id.cacel_post);
        post = findViewById(R.id.post_buton);
        title = findViewById(R.id.info_Title);
        message = findViewById(R.id.info_text);
        message.setMovementMethod(ScrollingMovementMethod.getInstance());
        progressbar = findViewById(R.id.p_Bar);

        //CID
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        CID =  bundle.getString("CID");
        //
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
                TITLE = title.getText().toString();
                MSG = message.getText().toString();
                new ListTask().execute();
                /********************************************************/
                // refresh announce board when add new announce

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

    private class ListTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            final AlertDialog.Builder attend_chooser = new AlertDialog.Builder(Information.this, R.style.CustomDialog);
            LayoutInflater inflater = Information.this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.progress, null);
            attend_chooser.setView(dialogView);
            alertDialog = attend_chooser.create();
            progressbar = dialogView.findViewById(R.id.p_Bar);
            alertDialog.show();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            result = backend.Communication(9,CID,TITLE,MSG);
            return null;
        }

        @Override
        protected void onPostExecute(Void d){
            Cursor cursor = getCursor();
            cursor.moveToFirst();
            String class_name = cursor.getString(cursor.getColumnIndex("cname"));
            String class_time = cursor.getString(cursor.getColumnIndex("ctime"));
            Intent intent1 = new Intent();
            intent1.setClass(Information.this,Teacher_class.class);
            Bundle bundle1 = new Bundle();
            bundle1.putString("data1",class_name);
            bundle1.putString("data2",class_time);
            bundle1.putString("data3",CID);
            intent1.putExtras(bundle1);
            Teacher_class.reset.finish();
            startActivity(intent1);
            Information.this.finish();
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
    private Cursor getCursor(){
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db=dbHelper.getReadableDatabase();  //透過dbHelper取得讀取資料庫的SQLiteDatabase物件，可用在查詢
        String[] columns={"_id", "cname", "ctime"};
        Cursor cursor = db.query("MyClass",columns,"_id=?",new String[]{CID},null,null,null);  //查詢所有欄位的資料
        return cursor;
    }
}
