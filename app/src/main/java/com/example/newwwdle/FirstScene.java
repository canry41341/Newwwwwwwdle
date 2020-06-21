package com.example.newwwdle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class FirstScene extends AppCompatActivity {
    Cursor cursor;
    SharedPreferences pref;
    private DBHelper dbHelper;
    boolean login_flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_scene);
        dbHelper = new DBHelper(this);
        new ListTask().execute();



    }

    private class ListTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            pref = getSharedPreferences("userdata", MODE_PRIVATE);
            login_flag = pref.getBoolean("login_flag", false);
            return null;
        }

        @Override
        protected void onPostExecute(Void d){
            if (login_flag) {
                String ID = pref.getString("ID", "Unknown");        // ID (Account)
                String PW = pref.getString("password", "Unknown");  // Password
                String type = pref.getString("IDtype", "Unknown");  // ID type
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                if (ID.equals("Unknown") | PW.equals("Unknown") | type.equals("Unknown")) {
                    pref.edit().putBoolean("login_flag", false).commit();   // Login error, set login_flag back to false
                }
                // Already log in as student, jump to student window
                else {
                    cursor = getCursor();
                    int length = cursor.getCount();
                    String mycourse_ID[] = new String[length];
                    String mycourse_name[] = new String[length];
                    String mycourse_time[] = new String[length];
                    int i=0;
                    while(cursor.moveToNext()){
                        mycourse_ID[i] = cursor.getString(cursor.getColumnIndex("_id"));
                        Log.d("cursorid", mycourse_ID[i]);
                        mycourse_name[i] = cursor.getString(cursor.getColumnIndex("cname"));
                        Log.d("cursorname", mycourse_name[i]);
                        mycourse_time[i] = cursor.getString(cursor.getColumnIndex("ctime"));
                        Log.d("cursortime", mycourse_time[i]);
                        i++;
                    }


                    if (type.equals("student")) {
                        intent.setClass(FirstScene.this, Student.class);
                        bundle.putString("id", ID);//send student ID to next activity
                        bundle.putString("name", pref.getString("name", "未知的使用者"));
                        bundle.putStringArray("s1", mycourse_name);
                        bundle.putStringArray("s2", mycourse_time);
                        bundle.putStringArray("s3", mycourse_ID);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                    // Already log in as teacher, jump to teacher window
                    else if (type.equals("teacher")) {
                        intent.setClass(FirstScene.this, teacher.class);
                        bundle.putString("id", ID);//send student ID to next activity
                        bundle.putString("name", pref.getString("name", "未知的使用者"));
                        bundle.putStringArray("s1", mycourse_name);
                        bundle.putStringArray("s2", mycourse_time);
                        bundle.putStringArray("s3", mycourse_ID);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
            }else{
                Intent intent = new Intent();
                intent.setClass(FirstScene.this, MainActivity.class);
                startActivity(intent);
            }
        }
    }

    private Cursor getCursor(){
        SQLiteDatabase db=dbHelper.getReadableDatabase();  //透過dbHelper取得讀取資料庫的SQLiteDatabase物件，可用在查詢
        String[] columns={"_id", "cname", "ctime"};
        Cursor cursor = db.query("MyClass",columns,null,null,null,null,null);  //查詢所有欄位的資料
        return cursor;
    }
}
