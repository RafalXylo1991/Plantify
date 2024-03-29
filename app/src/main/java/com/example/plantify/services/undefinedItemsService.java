package com.example.plantify.services;

import static com.example.plantify.notifications.CounterNotificationService.CHANNEL_ID;


import android.Manifest;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;


import com.example.plantify.R;

import java.util.Timer;
import java.util.TimerTask;

public class undefinedItemsService extends Service {
    RemoteViews notificationLayout;
    PendingIntent clickPI;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
         super.onStartCommand(intent, flags, startId);

         int event = intent.getIntExtra("events",0);
         int list = intent.getIntExtra("lists",0);
         int notice = intent.getIntExtra("notices",0);

        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                start(event,list,notice);

            }
        }, 2000,43200000);


        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.e("TAG", "onCreate");
        super.onCreate();

    }

    @Override
    public void onDestroy() {
        Log.e("tag", "onDestroy");

        super.onDestroy();


    }
    public void start(int event, int list, int notice){
        notificationLayout = new RemoteViews(getPackageName(),R.layout.notifications_left_things);

        notificationLayout.setTextViewText(R.id.leftEvents,"Current events "+event);
        notificationLayout.setTextViewText(R.id.leftLists,"Unfinished lists "+list);
        notificationLayout.setTextViewText(R.id.leftNotices,"Important notices "+notice);
        notificationLayout.setImageViewResource(R.id.eventIcon, R.drawable.event);
        notificationLayout.setImageViewResource(R.id.listIcon, R.drawable.todolist);
        notificationLayout.setImageViewResource(R.id.noticeIcon, R.drawable.notice);
        notificationLayout.setImageViewResource(R.id.img, R.drawable.currency);

        notificationLayout.setOnClickPendingIntent(R.id.click,clickPI);
        NotificationCompat.Builder      noti = new NotificationCompat.Builder(getBaseContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.event)

                .setContentTitle("cipka")
                .setContentText("cipka")
                .setCustomContentView(notificationLayout)

                .setPriority(NotificationCompat.PRIORITY_MAX);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getBaseContext());
        if(ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED)  {

            notificationManager.notify(546, noti.build());
        }
}}
