package com.example.newwwdle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {

    //點選完課程後進入到此class
    TextView className;

    String data1, data2, data3, ID; //data1用來存去你點哪個課程名稱
    //data2用來存你所點課程的上課時間

    String s1[], s2[], s3[]; //s1[]顯示課程名稱  s2[]顯示上課時間

    RecyclerView myRecyclerView;

    Backend backend = new Backend();
    //assign clock
    TextClock mClock;
    public Drawable dd;
    static Activity reset;
    String result;
    AlertDialog alertDialog;
    ProgressBar progressbar;
    Location lastKnownLocation;



    private Button atten_btn, state_btn; //分別是 "點名鈕"  "通知鈕"  "顯示點名狀態的按鈕"
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        SharedPreferences pref = getSharedPreferences("userdata", MODE_PRIVATE);
        ID = pref.getString("ID", "Unknown");


        className = findViewById(R.id.className);
        getData(); //接收你從上個activity傳來的參數
        setData(); //顯示你從上個activity傳來的參數
        reset = this;

        //set clock
        mClock = findViewById(R.id.clock);
        mClock.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/digital-7mt.ttf"));
        dd = getResources().getDrawable(R.drawable.button);


        myRecyclerView = findViewById(R.id.notifyView);
        myRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        //後端自己決定要從哪抓s1[]跟s2[]來源

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
                    lastKnownLocation = mLocationManager.getLastKnownLocation(locationProvider);
                    new ListTask().execute("rollcall");

                }
            }
        });


        //check點名狀態
        state_btn = findViewById(R.id.state_btn);
        state_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //進到下一個activity來檢視點名情況
                Toast.makeText(SecondActivity.this, "獲取資料中...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                intent.setClass(SecondActivity.this, StudentState.class);
                bundle.putString("CID", data3);//send student ID to next activity
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        new ListTask().execute("notify");
    }

    private class ListTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            if(params[0].equals("rollcall")){
                result = backend.Communication(11,data3);
            }else if(params[0].equals("notify")){
                result = backend.Communication(2,data3);
            }
            return params[0];
        }

        @Override
        protected void onPreExecute() {
            final AlertDialog.Builder attend_chooser = new AlertDialog.Builder(SecondActivity.this, R.style.CustomDialog);
            LayoutInflater inflater = SecondActivity.this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.progress, null);
            attend_chooser.setView(dialogView);
            alertDialog = attend_chooser.create();
            progressbar = dialogView.findViewById(R.id.p_Bar);
            progressbar.setClickable(false);
            alertDialog.setCancelable(false);
            alertDialog.show();

        }

        @Override
        protected void onPostExecute(String d){
            if(d.equals("rollcall")){
                String[] tokens = result.split(",");
                signin_permission = Boolean.parseBoolean(tokens[0].toLowerCase());
                teacher_long = Double.parseDouble(tokens[1]);
                teacher_lat = Double.parseDouble(tokens[2]);

                //Toast.makeText(SecondActivity.this,String.valueOf(signin_permission) + "/" + String.valueOf(teacher_lat) + "/" + String.valueOf(teacher_long),Toast.LENGTH_LONG).show();
                if (signin_permission) {
                    if (lastKnownLocation != null) {
                        float[] distance = new float[1];
                        Location.distanceBetween(teacher_lat, teacher_long, lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), distance);
                        System.out.println("distance : " + distance[0]);
                        Log.d("Location", "long:"+lastKnownLocation.getLongitude()+"\nlat: "+ lastKnownLocation.getLatitude());
                        if (distance[0] > 10000.0) {
                            Toast.makeText(SecondActivity.this, "你不在點名範圍裡！ (距離點名範圍" + distance[0] + "公尺)", Toast.LENGTH_LONG).show();
                        } else {
                                String sign = backend.Communication(8, ID, data3);
                                Toast.makeText(SecondActivity.this, "完成簽到", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SecondActivity.this, "獲取不到位置資訊哦！", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SecondActivity.this, "尚未開放點名喔！", Toast.LENGTH_SHORT).show();
                }
                alertDialog.dismiss();
            }else if(d.equals("notify")){
                showNotify(result);
                alertDialog.dismiss();
            }
        }
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
        NotifyAdapter notifyAdapter = new NotifyAdapter(this, s1, s2, s3, data3);
        myRecyclerView.setAdapter(notifyAdapter);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
