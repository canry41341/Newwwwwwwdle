package com.example.newwwdle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class StudentState extends AppCompatActivity {

    //這個class用的adapter是StateAdapter
    private String s1[]; //s1用來設定點名日期
    private String s2[]; //s2用來儲存當日點名狀態

    RecyclerView myRecyclerView;

    //
    private static Toast toast;

    private static void makeTextAndShow(final Context context, final String text, final int duration) {
        if (toast == null) {
            //如果還沒有用過makeText方法，才使用
            toast = android.widget.Toast.makeText(context, text, duration);
        } else {
            toast.setText(text);
            toast.setDuration(duration);
        }
        toast.show();
    }
    Backend backend = new Backend();
    String result,CID;
    //
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // refresh announce board when add new announce
            Cursor cursor = getCursor();
            cursor.moveToFirst();
            String class_name = cursor.getString(cursor.getColumnIndex("cname"));
            String class_time = cursor.getString(cursor.getColumnIndex("ctime"));
            Intent intent1 = new Intent();
            intent1.setClass(StudentState.this,SecondActivity.class);
            Bundle bundle1 = new Bundle();
            bundle1.putString("data1",class_name);
            bundle1.putString("data2",class_time);
            bundle1.putString("data3",CID);
            intent1.putExtras(bundle1);
            SecondActivity.reset.finish();
            startActivity(intent1);
            StudentState.this.finish();
            //Teacher_class.reset.onCreate(bundle1,null);
        }
        return false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_state);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        CID =  bundle.getString("CID");

        myRecyclerView = findViewById(R.id.stateRecyclerView);

        //
        SharedPreferences pref = getSharedPreferences("userdata", MODE_PRIVATE);
        String ID = pref.getString("ID", "Unknown");
        result = backend.Communication(7,ID,CID);
        //


        String[] tokens = result.split(";");
        makeTextAndShow(StudentState.this,tokens[0],toast.LENGTH_LONG);
        s1 = new String[tokens.length];
        s2 = new String[tokens.length];
        for (int i = 0; i<tokens.length; i++){
            String[] day_split = tokens[i].split("/");
            s1[i] = day_split[0].substring(5,6) + "月" + day_split[0].substring(7,9) + "號";
            s2[i] = day_split[1];
        }



        /*決定s1跟s2的來源
        s1 = getResources().getStringArray(R.array.date);
        s2 = getResources().getStringArray(R.array.state);
        */
        //設定adapter
        StateAdapter stateAdapter = new StateAdapter(this, s1, s2, CID);
        myRecyclerView.setAdapter(stateAdapter);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    private Cursor getCursor(){
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db=dbHelper.getReadableDatabase();  //透過dbHelper取得讀取資料庫的SQLiteDatabase物件，可用在查詢
        String[] columns={"_id", "cname", "ctime"};
        Cursor cursor = db.query("MyClass",columns,"_id=?",new String[]{CID},null,null,null);  //查詢所有欄位的資料
        return cursor;
    }
}
