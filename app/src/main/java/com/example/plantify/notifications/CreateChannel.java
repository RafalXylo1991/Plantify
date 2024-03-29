package com.example.plantify.notifications;




import static com.example.plantify.notifications.CounterNotificationService.CHANNEL_ID;

import android.app.NotificationChannel;
import android.app.NotificationManager;

public class CreateChannel {



    public NotificationChannel getChannel() {
        return channel;
    }

    public void setChannel(NotificationChannel channel) {
        this.channel = channel;
    }

    NotificationChannel channel;


    public CreateChannel(NotificationManager manager,String channelId){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "events", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Events notifications");

            manager.createNotificationChannel(channel);
        }
    }



}
