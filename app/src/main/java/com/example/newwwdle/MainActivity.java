package com.example.newwwdle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.IDNA;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewDebug;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;


public class MainActivity extends AppCompatActivity {

    private static final boolean DBG = Boolean.parseBoolean(null);
    private static final String TAG = "";
    public static String mDeviceIMEI = "0";
    InputMethodManager imm;
    Backend backend = new Backend();
    TelephonyManager mTelephonyManager = null;
    private ProgressDialog progressDialog;
    private EditText name, password;
    private Button login_btn;
    private DBHelper dbHelper;      // DB
    String IDname;
    String IDtype;
    String course[];
    String course_time[];
    String course_CID[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);  // Initialize DB
        name = findViewById(R.id.edit_name); //username(student ID)
        password = findViewById(R.id.edit_id);//password
        login_btn = findViewById(R.id.login_btn);

        // check if user is already log in
        final SharedPreferences pref = getSharedPreferences("userdata", MODE_PRIVATE);
        boolean login_flag = pref.getBoolean("login_flag", false);
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
                Cursor cursor = getCursor();
                int length = cursor.getCount();
                String mycourse_ID[] = new String[length];
                String mycourse_name[] = new String[length];
                String mycourse_time[] = new String[length];
                //cursor.moveToFirst();
                /*Log.d("cursorstring", cursorToString(cursor));
                Log.d("courseID", cursor.getString(0));
                Log.d("courseName", cursor.getString(1));
                Log.d("courseTime", cursor.getString(2));*/
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
                    intent.setClass(MainActivity.this, Student.class);
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
                    intent.setClass(MainActivity.this, teacher.class);
                    bundle.putString("id", ID);//send student ID to next activity
                    bundle.putString("name", pref.getString("name", "未知的使用者"));
                    bundle.putStringArray("s1", mycourse_name);
                    bundle.putStringArray("s2", mycourse_time);
                    bundle.putStringArray("s3", mycourse_ID);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        }

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().equals("") | password.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "請輸入帳號密碼", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();

                    // Get token and check if Account is locked
                    // Get token
                    FirebaseInstanceId.getInstance().getInstanceId()
                            .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                @Override
                                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                    if (!task.isSuccessful()) {
                                        Log.w(TAG, "getInstanceId failed", task.getException());
                                        return;
                                    }

                                    // Get new Instance ID token
                                    String token = task.getResult().getToken();
                                    pref.edit().putString("token", token).commit();
                                    // Log and toast
                                    // Toast.makeText(MainActivity.this, "TOKEN = "+token, Toast.LENGTH_SHORT).show();
                                }
                            });
                    String login_permission = backend.Communication(4, name.getText().toString(), pref.getString("token", null));
                    if (login_permission.equals("False")) {     // Account is locked
                        Toast.makeText(MainActivity.this, "請過一段時間後再登嘗試登入哦！", Toast.LENGTH_SHORT).show();
                    }
                    else if (login_permission.equals("Error")){
                        Toast.makeText(MainActivity.this, "Error，please connect developer", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        String result = backend.Communication(1, name.getText().toString(), password.getText().toString());//result是取得的整個字串
                        if (result.contains(";")) {
                            backend_split(result);
                            // Add name to SharedPreference
                            pref.edit().putString("name", IDname)
                                    .putString("IDtype", IDtype).commit();
                            // Add data to DB
                            for(int i=0;i<course.length;i++){
                                add(course_CID[i], course[i], course_time[i]);
                                Cursor cursor = dbHelper.getReadableDatabase().query("MyClass", null, null, null, null, null, null);
                                Log.d("CursorAdd", cursorToString(cursor));
                            }

                            switch (IDtype) {
                                case "student":
                                    intent.setClass(MainActivity.this, Student.class);
                                    bundle.putString("id", name.getText().toString());//send student ID to next activity
                                    bundle.putStringArray("s1", course);
                                    bundle.putStringArray("s2", course_time);
                                    bundle.putStringArray("s3", course_CID);
                                    bundle.putString("name", IDname);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    // Save ID and type, turn login_flag to true
                                    pref.edit().putBoolean("login_flag", true)
                                            .putString("ID", name.getText().toString())
                                            .putString("password", password.getText().toString()).commit();
                                    break;
                                case "teacher":
                                    intent.setClass(MainActivity.this, teacher.class);
                                    bundle.putString("id", name.getText().toString());//send student ID to next activity
                                    bundle.putStringArray("s1", course);
                                    bundle.putStringArray("s2", course_time);
                                    bundle.putStringArray("s3", course_CID);
                                    bundle.putString("name", IDname);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    // Save ID and type, turn login_flag to true
                                    pref.edit().putBoolean("login_flag", true)
                                            .putString("ID", name.getText().toString())
                                            .putString("password", password.getText().toString()).commit();
                                    break;
                            }
                        } else {
                            Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
                        }
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

    // Add data to DB
    private void add(String CID, String Cname, String Ctime){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("_id", CID);
        values.put("cname", Cname);
        values.put("ctime", Ctime);
        db.insert("MyClass", null, values);
    }

    // Get data from DB
    private Cursor getCursor(){
        SQLiteDatabase db=dbHelper.getReadableDatabase();  //透過dbHelper取得讀取資料庫的SQLiteDatabase物件，可用在查詢
        String[] columns={"_id", "cname", "ctime"};
        Cursor cursor = db.query("MyClass",columns,null,null,null,null,null);  //查詢所有欄位的資料
        return cursor;
    }

    private void backend_split(String result) {
        String results[] = result.split(";");//切開

        IDname = results[0];
        IDtype = results[1];
        int len = (results.length - 2) / 4;//計算課程數
        course_CID = new String[len];
        course = new String[len];
        course_time = new String[len];
        String course_place[] = new String[len];
        String course_time_d[];
        String course_time_h[] = new String[2];
        String course_time_hh[] = new String[2];
        for (int i = 2; i < len + 2; i++) {//課程ID
            course_CID[i - 2] = results[i];
        }
        for (int j = len + 2; j < (len * 2) + 2; j++) {//課程名稱
            course[j - len - 2] = results[j];
        }
        for (int k = (len * 2) + 2; k < (len * 3) + 2; k++) {//課程時間
            course_time[k - len * 2 - 2] = results[k];
        }
        for (int l = (len * 3) + 2; l < results.length; l++) {//課程地點
            course_place[l - len * 3 - 2] = results[l];
        }
        for (int i = 0; i < len; i++) {
            course_time_d = course_time[i].split("d");//course_time_d[0] = 星期幾
            switch (course_time_d[0]) {
                case "1":
                    course_time_d[0] = "一";
                    break;
                case "2":
                    course_time_d[0] = "二";
                    break;
                case "3":
                    course_time_d[0] = "三";
                    break;
                case "4":
                    course_time_d[0] = "四";
                    break;
                case "5":
                    course_time_d[0] = "五";
                    break;
            }
            course_time_h[0] = course_time_d[1].substring(0, 1);
            course_time_h[1] = course_time_d[1].substring(2, 3);
            switch (course_time_h[0]) {
                case "1":
                    course_time_hh[0] = "08:10";
                    break;
                case "2":
                    course_time_hh[0] = "09:10";
                    break;
                case "3":
                    course_time_hh[0] = "10:10";
                    break;
                case "4":
                    course_time_hh[0] = "11:10";
                    break;
                case "5":
                    course_time_hh[0] = "13:10";
                    break;
                case "6":
                    course_time_hh[0] = "14:10";
                    break;
                case "7":
                    course_time_hh[0] = "15:10";
                    break;
                case "8":
                    course_time_hh[0] = "16:10";
                    break;
                case "9":
                    course_time_hh[0] = "17:10";
                    break;
            }
            switch (course_time_h[1]) {
                case "1":
                    course_time_hh[1] = "09:00";
                    break;
                case "2":
                    course_time_hh[1] = "10:00";
                    break;
                case "3":
                    course_time_hh[1] = "11:00";
                    break;
                case "4":
                    course_time_hh[1] = "12:00";
                    break;
                case "5":
                    course_time_hh[1] = "14:00";
                    break;
                case "6":
                    course_time_hh[1] = "15:00";
                    break;
                case "7":
                    course_time_hh[1] = "16:00";
                    break;
                case "8":
                    course_time_hh[1] = "17:00";
                    break;
                case "9":
                    course_time_hh[1] = "18:00";
                    break;
            }
            course_time[i] = "[" + course_time_d[0] + "]" + course_time_hh[0] + "~" + course_time_hh[1] + course_place[i];
        }
    }

    @Override
    protected void onDestroy() {            //當銷毀該app時
        super.onDestroy();
        try {
            backend.bw.flush();
            backend.bw.close();
            backend.br.close();
            backend.clientSocket.close();
            dbHelper.close();   // close DB
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e("text", "onDestroy()=" + e.toString());
        }
    }

    @Override
    public void onBackPressed() {
        //Dismiss Progress Dialog
        progressDialog.dismiss();
    }

    public String cursorToString(Cursor cursor){
        String cursorString = "";
        if (cursor.moveToFirst() ){
            String[] columnNames = cursor.getColumnNames();
            for (String name: columnNames)
                cursorString += String.format("%s ][ ", name);
            cursorString += "\n";
            do {
                for (String name: columnNames) {
                    cursorString += String.format("%s ][ ",
                            cursor.getString(cursor.getColumnIndex(name)));
                }
                cursorString += "\n";
            } while (cursor.moveToNext());
        }
        return cursorString;
    }

}
