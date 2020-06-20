package com.example.newwwdle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

public class ShowNotify extends AppCompatActivity {

    TextView Title , message , time;
    String title , date, msg, CID , changing_tag;
    String class_name , class_time;
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Toast.makeText(this, "正在更新公告...", Toast.LENGTH_SHORT).show();
            // refresh announce board when add new announce
            Cursor cursor = getCursor();
            cursor.moveToFirst();
            Bundle bundle1 = new Bundle();
            bundle1.putString("data1",class_name);
            bundle1.putString("data2",class_time);
            bundle1.putString("data3",CID);
            class_name = cursor.getString(cursor.getColumnIndex("cname"));
            class_time = cursor.getString(cursor.getColumnIndex("ctime"));
            if(changing_tag.equals("student")) {
                Intent intent1 = new Intent();
                intent1.setClass(ShowNotify.this, SecondActivity.class);
                intent1.putExtras(bundle1);
                SecondActivity.reset.finish();
                startActivity(intent1);

            }


            ShowNotify.this.finish();
        }
        return false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("MSG","notify");


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        title =  bundle.getString("title");
        date = bundle.getString("time");
        msg = bundle.getString("msg");
        CID = bundle.getString("CID");//沒東西
        Log.d("MSG",CID);
        changing_tag = bundle.getString("tag");

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
    private Cursor getCursor(){
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db=dbHelper.getReadableDatabase();  //透過dbHelper取得讀取資料庫的SQLiteDatabase物件，可用在查詢
        String[] columns={"_id", "cname", "ctime"};
        Cursor cursor = db.query("MyClass",columns,"_id=?",new String[]{CID},null,null,null);  //查詢所有欄位的資料
        return cursor;
    }
}
