package com.example.newwwdle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
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
    TextView className;
    String data1, data2;
    AlertDialog alertDialog , alertDialog_noty;
    boolean start , enable;
    CountDownTimer cdt;
    TextClock mycheckclock;
    Button attend_now_btn , attend_all_btn;
    public Drawable dd;
    String ss1[], ss2[];

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
        start = false;
        enable = false;

        mycheckclock = findViewById(R.id.textClock);
        mycheckclock.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/digital-7mt.ttf"));
        dd = getResources().getDrawable(R.drawable.button);

        /*************************抓課程名稱跟時間**************************************/
        ss1 = getResources().getStringArray(R.array.class_Name);
        ss2 = getResources().getStringArray(R.array.time);
        /*********************************************************************************/

        myTRecyclerView = findViewById(R.id.teacher_noty);
        myTRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        className = findViewById(R.id.className1);
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
                            atttend_btn.setText("結束點名");
                            atttend_btn.setBackgroundColor(Color.parseColor("#ffca28"));
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
                                    teacher_long = lastKnownLocation.getLongitude();
                                    teacher_lat = lastKnownLocation.getLatitude();
                                    Toast.makeText(Teacher_class.this, "經度:" + teacher_long + "\n緯度:" + teacher_lat, Toast.LENGTH_SHORT).show();
                                    Toast.makeText(Teacher_class.this, "點名開始", Toast.LENGTH_SHORT).show();
                                    /*************************TIMER**************************/
                                    cdt = new CountDownTimer(numberPicker.getValue()*6000, 1000) {
                                        @Override
                                        public void onTick(long millisUntilFinished) {
                                            Toast.makeText(Teacher_class.this, Long.toString(millisUntilFinished/1000)+"秒", Toast.LENGTH_SHORT).show();
                                        }
                                        @Override
                                        public void onFinish() {
                                            //database 點名停止
                                            String result = backend.Communication(10,"CID1",0,teacher_long,teacher_lat);
                                            Toast.makeText(Teacher_class.this, result, Toast.LENGTH_SHORT).show();
                                            atttend_btn.setText("開啟點名");
                                            atttend_btn.setBackgroundDrawable(dd);
                                            //Toast.makeText(Teacher_class.this, "停止點名", Toast.LENGTH_SHORT).show();
                                            start = false;
                                        }
                                    };
                                    cdt.start();
                                    //databse 點名開始;
                                    //
                                    String result = backend.Communication(10,"CID1",1,teacher_long,teacher_lat);
                                    Toast.makeText(Teacher_class.this, result, Toast.LENGTH_SHORT).show();
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
                    start = true;

                } else{//中斷點名
                    String result = backend.Communication(10,"CID1",0,teacher_long,teacher_lat);
                    Toast.makeText(Teacher_class.this, result, Toast.LENGTH_SHORT).show();
                    cdt.cancel();
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
                Intent intent = new Intent();
                intent.setClass(Teacher_class.this, Information.class);
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
                        Toast.makeText(Teacher_class.this, "all click success!", Toast.LENGTH_SHORT).show();
                    }
                });

                attend_now_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Teacher_class.this, TeacherState.class));
                        Toast.makeText(Teacher_class.this, "now click success!", Toast.LENGTH_SHORT).show();
                    }
                });


                alertDialog_noty.show();



            }
        });


        getData();
        setData();
        showNotify();

    }

    private void getData() {
        if (getIntent().hasExtra("data1") && getIntent().hasExtra("data2")) {

            data1 = getIntent().getStringExtra("data1");
            data2 = getIntent().getStringExtra("data2");
        } else {
            Toast.makeText(this, "No data!", Toast.LENGTH_SHORT).show();
        }
    }

    private void setData() {

        className.setText(data1);
    }

    private void showNotify() {
        TnotifyAdapter notifyAdapter = new TnotifyAdapter(Teacher_class.this, ss1, ss2);
        myTRecyclerView.setAdapter(notifyAdapter);
        myTRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}
