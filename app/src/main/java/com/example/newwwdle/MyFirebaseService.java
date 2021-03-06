package com.example.newwwdle;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.icu.text.RelativeDateTimeFormatter;
import android.net.DhcpInfo;
import android.os.Build;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseService extends FirebaseMessagingService {

    static  String token = "hello";
    DBHelper dbHelper;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){
        super.onMessageReceived(remoteMessage);
// Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d("MyFirebaseService", "Message data payload: " + remoteMessage.getData());
            String CID = remoteMessage.getData().get("CID");
            String cname = remoteMessage.getData().get("CNAME");
            String ctime = remoteMessage.getData().get("CTIME");
            String title = remoteMessage.getData().get("TITLE");
            String body = remoteMessage.getData().get("Announce");
            SendNotification2(CID, cname, ctime, title, body);
        }

    }

    @Override
    public void onNewToken(String s){
        super.onNewToken(s);
        token = s;
        SharedPreferences pref = getSharedPreferences("userdata", MODE_PRIVATE);
        pref.edit().putString("token",s).commit();
        Log.i("MyFirebaseService", "token"+s);
    }

    // Send Notification (CID, msg_title, msg_content)
    private void SendNotification(String CID, String msg_title, String msg_content){
        // get class_name and class_time from local DB
        dbHelper = new DBHelper(this);
        Cursor cursor = getCursor(CID);
        Log.e("Cursor", cursorToString(cursor));
        cursor.moveToFirst();
        String class_name = cursor.getString(cursor.getColumnIndex("cname"));
        String class_time = cursor.getString(cursor.getColumnIndex("ctime"));
        dbHelper.close();

        // Intent
        // open SecondActivity with parameters data1 and data2
        Intent intent = new Intent(this, SecondActivity.class);
        intent.putExtra("data1", class_name);
        intent.putExtra("data2", class_time);
        intent.putExtra("data3", CID);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pintent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        // NotificationManager
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Notification Builder
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.drawable.ic_stat_name)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon))
                .setContentTitle(msg_title)     // set notification title
                .setContentText(msg_content)    // set notification content
                .setAutoCancel(true)            // notification can be closed
                .setDefaults(Notification.DEFAULT_ALL)      // use default vibration mode, LED mode, sound mode
                .setWhen(System.currentTimeMillis())        // display the time notification received
                .setContentIntent(pintent)                 // set intent which will launch after notification be clicked
                .setShowWhen(true);
        if (Build.VERSION.SDK_INT > 25) {
            // Notification Channel (for API 26+)
            NotificationChannel notificationChannel = new NotificationChannel("channel1", "課程通知", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("接收課程異動通知");
            notificationManager.createNotificationChannel(notificationChannel);
            builder.setChannelId("channel1");
        }

        // Notification to show
        Notification notification;
        notification = builder.setStyle(new Notification.BigTextStyle().bigText(msg_content)).build();

        // delete the old notification with id
        notificationManager.cancel(0);
        // notify notification with id
        notificationManager.notify(0, notification);
        //Toast.makeText(this, "notify~~~" + Build.VERSION.SDK_INT, Toast.LENGTH_SHORT).show();
    }

    // Get data from DB
    private Cursor getCursor(String CID){
        SQLiteDatabase db=dbHelper.getReadableDatabase();  //透過dbHelper取得讀取資料庫的SQLiteDatabase物件，可用在查詢
        String[] columns={"_id", "cname", "ctime"};
        Cursor cursor = db.query("MyClass",columns,"_id=?", new String[]{CID},null,null,null);  //查詢所有欄位的資料
        return cursor;
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



    private void SendNotification2(String CID, String cname, String ctime, String msg_title, String msg_content){
        // Intent
        // open SecondActivity with parameters data1 and data2
        Intent intent = new Intent(this, SecondActivity.class);
        intent.putExtra("data1", cname);
        intent.putExtra("data2", ctime);
        intent.putExtra("data3", CID);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pintent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        // NotificationManager
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Notification Builder
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.drawable.ic_stat_name)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon))
                .setContentTitle(msg_title)     // set notification title
                .setContentText(msg_content)    // set notification content
                .setAutoCancel(true)            // notification can be closed
                .setDefaults(Notification.DEFAULT_ALL)      // use default vibration mode, LED mode, sound mode
                .setWhen(System.currentTimeMillis())        // display the time notification received
                .setContentIntent(pintent)                 // set intent which will launch after notification be clicked
                .setShowWhen(true);
        if (Build.VERSION.SDK_INT > 25) {
            // Notification Channel (for API 26+)
            NotificationChannel notificationChannel = new NotificationChannel("channel1", "課程通知", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("接收課程異動通知");
            notificationManager.createNotificationChannel(notificationChannel);
            builder.setChannelId("channel1");
        }

        // Notification to show
        Notification notification;
        notification = builder.setStyle(new Notification.BigTextStyle().bigText(msg_content)).build();

        // delete the old notification with id
        notificationManager.cancel(0);
        // notify notification with id
        notificationManager.notify(0, notification);
    }
}
