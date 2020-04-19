package com.example.newwwdle;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
        dbHelper = new DBHelper(this);

        if(remoteMessage != null){
            Log.i("MyFirebaseService", "title"+remoteMessage.getNotification().getTitle());
            Log.i("MyFirebaseService", "body"+remoteMessage.getNotification().getBody());
            SendNotification("1", remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }
        // close DB
        dbHelper.close();
    }

    @Override
    public void onNewToken(String s){
        super.onNewToken(s);
        token = s;
        Log.i("MyFirebaseService", "token"+s);
    }

    // Send Notification (CID, msg_title, msg_content)
    private void SendNotification(String CID, String msg_title, String msg_content){

        // get class_name and class_time from local DB
        Cursor cursor = getCursor(CID);
        String class_name = cursor.getString(1);
        String class_time = cursor.getString(2);

        // Intent
        // open SecondActivity with parameters data1 and data2
        Intent intent = new Intent(this, SecondActivity.class);
        intent.putExtra("data1", class_name);
        intent.putExtra("data2", class_time);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pintent = PendingIntent.getActivity(this, 0, intent, 0);

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
}
