package com.example.plantify.services;



import static com.example.plantify.notifications.CounterNotificationService.CHANNEL_ID;

import android.Manifest;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;


import com.example.plantify.MainActivity;
import com.example.plantify.R;
import com.example.plantify.login.loginView;
import com.example.plantify.Models.PictureNotice.Event;
import com.example.plantify.Models.PictureNotice.users;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public class backgroundService  extends Service {
    Timer timer;
    TimerTask timerTask;
    String[] courses = { "1h", "4h",
            "12h", "1D",
            "7D", "10D" };
    String TAG = "Timers";
    Event event;
    int Your_X_SECS = 5;
    Date date;
   ArrayList<Event> lista = new ArrayList<>();
    long time = 0;
    int id =4;
    NotificationCompat.Builder noti;
    RemoteViews notificationLayout;
    users user;
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        lista = intent.getParcelableArrayListExtra("event");
        Log.i("lista", String.valueOf(lista.size()));
        date = (Date)intent.getSerializableExtra("date");
        user = intent.getParcelableExtra("user");
        Log.i("cipeczka", String.valueOf(date));

        switch (lista.get(0).getRemindTime()){
            case "1h":
                time = 60*60*1000;
                break;
            case "4h":
                time = 4*60*60*1000;
                break;
            case "12h":
                time = 12*60*60*1000;
                break;
            case "1D":
                time = 24*60*60*1000;
                break;
            case "7D":
                time = 7*24*60*60*1000;
                break;
            case "10D":
                time = 10*24*60*60*1000;
                break;
        }



        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                startTimer();

            }
        }, 10000);

        return START_STICKY;
    }


    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");


    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        stoptimertask();
        super.onDestroy();


    }


    final Handler handler = new Handler();


    public void startTimer() {
        //set a new Timer
        Date d ;

        SimpleDateFormat formatter2=new SimpleDateFormat("dd-MM-yyyy");
        try {
            d =formatter2.parse(lista.get(0).getStartDate());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        lista.forEach(new Consumer<Event>() {
            @Override
            public void accept(Event event) {

                timer = new Timer();

                timerTask=new TimerTask() {
                    @Override
                    public void run() {
                        Intent intent;
                        if(user.isLogged){
                            intent = new Intent(getBaseContext(), MainActivity.class);
                        }else{
                            intent = new Intent(getBaseContext(), loginView.class);
                        }

                        intent.putExtra("user",user);
                        intent.putExtra("event",event);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        DateFormat formatter = new SimpleDateFormat("HH:mm");
                        int hours = 24-new Date().getHours();
                        int hours2 = 0;
                        try {
                            hours2 = formatter.parse(event.getStartTime()).getHours();
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        notificationLayout = new RemoteViews(getPackageName(),R.layout.notification_layout);
                        PendingIntent clickPI=PendingIntent
                                .getActivity(getBaseContext(), 0,
                                        intent,
                                        PendingIntent.FLAG_UPDATE_CURRENT);
                        notificationLayout.setTextViewText(R.id.title,event.getTitle());
                        notificationLayout.setTextViewText(R.id.desc,event.getDesc());
                        notificationLayout.setTextViewText(R.id.time,"Left "+(hours2+hours)+" Hours and ");
                        notificationLayout.setImageViewResource(R.id.img, R.drawable.currency);

                        notificationLayout.setOnClickPendingIntent(R.id.click,clickPI);





                  NotificationCompat.Builder      noti = new NotificationCompat.Builder(getBaseContext(), CHANNEL_ID)
                                .setSmallIcon(R.drawable.event)

                                .setContentTitle(event.getTitle())
                                .setContentText(event.getStartDate())
                                .setCustomContentView(notificationLayout)

                                .setPriority(NotificationCompat.PRIORITY_MAX);



                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getBaseContext());
                        if(ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.POST_NOTIFICATIONS) ==
                                PackageManager.PERMISSION_GRANTED)  {
                            Log.i("TAG", String.valueOf(event.getId()));
                            notificationManager.notify(event.getId(), noti.build());


                        }



                    }
                };
                timer.schedule(timerTask, date,20000); //
            }
        });


    }

    public void stoptimertask() {

        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeTimerTask(Event event) {

        timerTask = new TimerTask() {
            public void run() {


                handler.post(new Runnable() {
                    public void run() {





                    }
                });
            }
        };
    }

}
