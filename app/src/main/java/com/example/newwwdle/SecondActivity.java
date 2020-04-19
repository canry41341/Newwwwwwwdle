package com.example.newwwdle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {

    //點選完課程後進入到此class
    TextView className;

    String data1, data2; //data1用來存去你點哪個課程名稱
    //data2用來存你所點課程的上課時間

    String s1[], s2[], s3[]; //s1[]顯示課程名稱  s2[]顯示上課時間

    RecyclerView myRecyclerView;

    Backend backend = new Backend();
    //assign clock
    TextClock mClock;
    public Drawable dd;


    private Button atten_btn, notice_btn, state_btn; //分別是 "點名鈕"  "通知鈕"  "顯示點名狀態的按鈕"
    double teacher_long = 999;    // teacher's longitude (get from server, default 999)經度
    double teacher_lat = 999;     // teacher's latitude (get from server, default 999)緯度
    boolean signin_permission = false;   // student sign in permission (get from server)開啟點名

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

    //test
    TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        className = findViewById(R.id.className);

        //set clock
        mClock = findViewById(R.id.clock);
        mClock.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/digital-7mt.ttf"));
        dd = getResources().getDrawable(R.drawable.button);


        myRecyclerView = findViewById(R.id.notifyView);
        myRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        //後端自己決定要從哪抓s1[]跟s2[]來源
        //s1 = getResources().getStringArray(R.array.class_Name);
        //  s2 = getResources().getStringArray(R.array.time);

        // GPS parameter setting
        final LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        final String locationProvider = LocationManager.GPS_PROVIDER;   // Or use LocationManager.NETWORK_PROVIDER

        //check 點名是否成功
        atten_btn = findViewById(R.id.attendence_btn);
        atten_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check if Activity has ACCESS_FINE_LOCATION permission
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(SecondActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                    return;
                } else {
                    mLocationManager.requestLocationUpdates(locationProvider, 1000, 10, locationListener);
                    Location lastKnownLocation = mLocationManager.getLastKnownLocation(locationProvider);
                    String result = backend.Communication(11,"CID1");
                    String[] tokens = result.split(",");
                    signin_permission = Boolean.parseBoolean(tokens[0].toLowerCase());
                    teacher_lat = Double.parseDouble(tokens[1]);
                    teacher_long = Double.parseDouble(tokens[2]);

                    //Toast.makeText(SecondActivity.this,String.valueOf(signin_permission) + "/" + String.valueOf(teacher_lat) + "/" + String.valueOf(teacher_long),Toast.LENGTH_LONG).show();
                    if (signin_permission) {
                        if (lastKnownLocation != null) {
                            float[] distance = new float[1];
                            Location.distanceBetween(teacher_lat, teacher_long, lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), distance);
                            if (distance[0] > 50.0) {
                                Toast.makeText(SecondActivity.this, "你不在點名範圍裡！ (距離點名範圍" + distance[0] + "公尺)", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(SecondActivity.this, "已完成簽到", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SecondActivity.this, "獲取不到位置資訊哦！", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SecondActivity.this, "尚未開放點名喔！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });





        //check點名狀態
        state_btn = findViewById(R.id.state_btn);
        state_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //進到下一個activity來檢視點名情況

                startActivity(new Intent(SecondActivity.this, StudentState.class));
            }
        });

        String result = backend.Communication(2,"CID1");
        showNotify(result);//show notification recyclerview
        getData(); //接收你從上個activity傳來的參數
        setData(); //顯示你從上個activity傳來的參數
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

    private void showNotify(String result) {
        //split string

        String[] tokens = result.split(";");
        s1 = new String[tokens.length]; //title
        s2 = new String[tokens.length]; //time
        s3 = new String[tokens.length]; //msg
        for(int i=0; i < tokens.length; i++){
            String[] announces_split = tokens[i].split("/");
            s1[i] = announces_split[3];
            String temp = announces_split[2].substring(5);
            System.out.println(temp);
            String[] month = temp.split("m");
            s2[i] = month[0] + "-" + month[1].substring(0,month[1].length()-1);
            s3[i] = announces_split[1];
        }

        //設定adapter
        NotifyAdapter notifyAdapter = new NotifyAdapter(this, s1, s2, s3);
        myRecyclerView.setAdapter(notifyAdapter);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
