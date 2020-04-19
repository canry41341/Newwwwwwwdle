package com.example.newwwdle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Teacher_class extends AppCompatActivity  {

    private Button atttend_btn, info_btn, noti_btn;
    TextView className , count_down_time;

    String data1, data2, data3;
    AlertDialog alertDialog , alertDialog_noty;
    boolean start , enable;
    CountDownTimer cdt;
    TextClock mycheckclock;
    int minutes , seconds;
    Button attend_now_btn , attend_all_btn;
    public Drawable dd;
    String ss1[], ss2[], ss3[];
    String[] date;
    String[][] student;

    RecyclerView myTRecyclerView;

    double teacher_long;    // teacher's longitude (pass to server)
    double teacher_lat;     // teacher's latitude (pass to server)
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;   // for GPS permission checking

    // Define a listener that responds to location updates
    LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    };

    //
    Backend backend = new Backend();
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_class);



        className = findViewById(R.id.className1);
        getData();
        setData();


        start = false;
        enable = false;
        minutes = 0;
        seconds = 0;

        mycheckclock = findViewById(R.id.textClock);
        mycheckclock.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/digital-7mt.ttf"));
        dd = getResources().getDrawable(R.drawable.button);
        count_down_time = findViewById(R.id.time_countdown);
        count_down_time.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/digital-7mt.ttf"));
        count_down_time.setVisibility(View.INVISIBLE);
        count_down_time.setTextColor(Color.RED);

        /*************************抓公告**************************************/
        String result = backend.Communication(2,data3);
        System.out.println("aaa: " + result);
        String[] tokens = result.split(";");
        ss1 = new String[tokens.length]; //title
        ss2 = new String[tokens.length]; //time
        ss3 = new String[tokens.length]; //msg
        for(int i=0; i < tokens.length; i++){
            String[] announces_split = tokens[i].split("/");
            ss1[i] = announces_split[3];
            String temp = announces_split[2].substring(5);
            //System.out.println(temp);
            String[] month = temp.split("m");
            ss2[i] = month[0] + "-" + month[1].substring(0,month[1].length()-1);
            ss3[i] = announces_split[1];
        }

        /*********************************************************************************/

        myTRecyclerView = findViewById(R.id.teacher_noty);
        myTRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


        atttend_btn = findViewById(R.id.attendence_btn1);
        info_btn = findViewById(R.id.notification_btn1);

        final String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

        //Toast.makeText(Teacher_class.this, currentTime, Toast.LENGTH_SHORT).show();


        noti_btn = findViewById(R.id.status_btn1);//


        // GPS parameter setting
        final LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        final String locationProvider = LocationManager.GPS_PROVIDER;   // Or use LocationManager.NETWORK_PROVIDER

        atttend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //老師想要點名所以她按了這個按鈕
                enable = true;
                if (!start) {
                    final AlertDialog.Builder d = new AlertDialog.Builder(Teacher_class.this);
                    LayoutInflater inflater = Teacher_class.this.getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.dialog, null);
                    d.setTitle("設定點名時間");
                    d.setView(dialogView);

                    final NumberPicker numberPicker = (NumberPicker) dialogView.findViewById(R.id.dialog_number_picker);
                    numberPicker.setMaxValue(30);
                    numberPicker.setMinValue(10);
                    numberPicker.setWrapSelectorWheel(false);

                    d.setPositiveButton("開始點名", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, final int i) {

                            //Log.d(TAG, "onClick: " + numberPicker.getValue());
                            // Get teacher's GPS location
                            // check if Activity has ACCESS_FINE_LOCATION permission
                            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(Teacher_class.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                            } else {
                                assert mLocationManager != null;
                                // ask for location updates
                                mLocationManager.requestLocationUpdates(locationProvider, 1000, 10, locationListener);
                                // get the last known location
                                Location lastKnownLocation = mLocationManager.getLastKnownLocation(locationProvider);
                                if (lastKnownLocation != null) {
                                    atttend_btn.setText("結束點名");
                                    atttend_btn.setBackgroundColor(Color.parseColor("#ffca28"));
                                    start = true;
                                    teacher_long = lastKnownLocation.getLongitude();
                                    teacher_lat = lastKnownLocation.getLatitude();
                                    Toast.makeText(Teacher_class.this, "經度:" + teacher_long + "\n緯度:" + teacher_lat, Toast.LENGTH_SHORT).show();
                                    Toast.makeText(Teacher_class.this, "點名開始", Toast.LENGTH_SHORT).show();
                                    /*************************TIMER**************************/
                                    cdt = new CountDownTimer(numberPicker.getValue()*6000, 1000) { // 1Minute
                                        @Override
                                        public void onTick(long millisUntilFinished) {

                                            minutes = (int) ((millisUntilFinished+1000)/1000/60);
                                            seconds = (int) ((millisUntilFinished+1000)/1000%60);

                                            count_down_time.setText(String.format("%02d", minutes)+":"+String.format("%02d", seconds));
                                        }
                                        @Override
                                        public void onFinish() {
                                            //database 點名停止
                                            String result = backend.Communication(10,data3,0,teacher_long,teacher_lat);
                                            Toast.makeText(Teacher_class.this,"cancel", Toast.LENGTH_SHORT).show();
                                            mycheckclock.setVisibility(View.VISIBLE);
                                            count_down_time.setVisibility(View.INVISIBLE);
                                            atttend_btn.setText("開啟點名");
                                            atttend_btn.setBackgroundDrawable(dd);
                                            //Toast.makeText(Teacher_class.this, "停止點名", Toast.LENGTH_SHORT).show();
                                            start = false;
                                        }
                                    };
                                    cdt.start();
                                    //databse 點名開始;
                                    String result = backend.Communication(10,data3,1,teacher_long,teacher_lat);
                                    Toast.makeText(Teacher_class.this, "start sign", Toast.LENGTH_SHORT).show();
                                    count_down_time.setVisibility(View.VISIBLE);
                                    mycheckclock.setVisibility(View.INVISIBLE);
                                    /****************************TIMER***********************/
                                } else {
                                    Toast.makeText(Teacher_class.this, "獲取不到位置資訊哦！", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });

                    d.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            atttend_btn.setText("開啟點名");
                            atttend_btn.setBackgroundDrawable(dd);
                            Toast.makeText(Teacher_class.this, "停止點名", Toast.LENGTH_SHORT).show();
                            start = false;
                        }
                    });
                    alertDialog = d.create();
                    alertDialog.show();
                    alertDialog.setCanceledOnTouchOutside(false);


                } else{//中斷點名
                    String result = backend.Communication(10,data3,0,teacher_long,teacher_lat);
                    Toast.makeText(Teacher_class.this, "cancel", Toast.LENGTH_SHORT).show();
                    cdt.cancel();
                    count_down_time.setVisibility(View.INVISIBLE);
                    mycheckclock.setVisibility(View.VISIBLE);
                    //database 點名停止
                    atttend_btn.setText("開啟點名");
                    atttend_btn.setBackgroundDrawable(dd);
                    Toast.makeText(Teacher_class.this, "停止點名", Toast.LENGTH_SHORT).show();
                    start = false;
                }

            }
        });



        //發布通知

        info_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent();
                intent.setClass(Teacher_class.this, Information.class);
                startActivity(intent);

                 */
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                intent.setClass(Teacher_class.this, Information.class);
                bundle.putString("CID", data3);//send student ID to next activity
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        noti_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder attend_chooser = new AlertDialog.Builder(Teacher_class.this, R.style.CustomDialog);
                LayoutInflater inflater = Teacher_class.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.noti_dialog, null);
                attend_chooser.setView(dialogView);
                alertDialog_noty = attend_chooser.create();

                attend_all_btn = dialogView.findViewById(R.id.attend_all_btn);
                attend_now_btn = dialogView.findViewById(R.id.attend_curr_btn);
                /************************沒有點名的話***********/
                //if(點名 = 0){
                //attend_now_btn.setEnabled(false);
                //Toast.makeText(Teacher_class.this, "尚未開啟點名", Toast.LENGTH_SHORT).show();
                //}
                /******************************************/
                attend_all_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //startActivity(new Intent(Teacher_class.this, TeacherCheck.class));
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        intent.setClass(Teacher_class.this, TeacherCheck.class);
                        bundle.putString("CID", data3);//send student ID to next activity
                        intent.putExtras(bundle);
                        startActivity(intent);
                        Toast.makeText(Teacher_class.this, "all click success!", Toast.LENGTH_SHORT).show();
                    }
                });

                attend_now_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //
                        int temp = 0;
                        int today = 0;
                        String result = backend.Communication(3,data3);
                        System.out.println("aaa: " + result);
                        String[] tokens = result.split(";");
                        for(int i = 0; i < tokens.length; i++){
                            String[] ID_split = tokens[i].split(":");
                            String[] day_split = ID_split[1].split(",");
                            if(temp == 0){
                                student = new String[day_split.length+1][tokens.length+1];
                                date = new String[day_split.length];
                                today = day_split.length;
                                temp += 1;
                            }
                            student[0][i+1] = ID_split[0];
                            for(int j = 0; j < day_split.length; j++){
                                System.out.println(day_split[j]);
                                String[] check_split = day_split[j].split("/");
                                student[j+1][0] = check_split[0].substring(5);
                                date[j] = check_split[0].substring(5);
                                student[j+1][i+1] = check_split[1];
                            }
                        }
                        String[] class_student = new String[tokens.length];
                        String[] check = new String[tokens.length];
                        for(int i = 0; i < tokens.length; i++){
                            class_student[i] = student[0][i+1];
                            check[i] = student[today][i+1];
                            System.out.println("cid: " + class_student[i]);
                            System.out.println("check: " + check[i]);
                        }
                        //System.out.println(date);
                        Intent intt = new Intent();
                        intt.setClass(Teacher_class.this, TeacherState.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("data1",student[today][0]);
                        bundle.putStringArray("student",class_student);
                        bundle.putStringArray("check",check);
                        bundle.putString("CID",data3);
                        intt.putExtras(bundle);
                        startActivity(intt);
                        Toast.makeText(Teacher_class.this, "now click success!", Toast.LENGTH_SHORT).show();
                    }
                });


                alertDialog_noty.show();



            }
        });


        showNotify();

    }

    private void getData() {
        if (getIntent().hasExtra("data1") && getIntent().hasExtra("data2")) {

            data1 = getIntent().getStringExtra("data1");
            data2 = getIntent().getStringExtra("data2");
            data3 = getIntent().getStringExtra("data3");
        } else {
            Toast.makeText(this, "No data!", Toast.LENGTH_SHORT).show();
        }
    }

    private void setData() {

        className.setText(data1);
    }

    private void showNotify() {
        TnotifyAdapter notifyAdapter = new TnotifyAdapter(Teacher_class.this, ss1, ss2, ss3);
        myTRecyclerView.setAdapter(notifyAdapter);
        myTRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}
