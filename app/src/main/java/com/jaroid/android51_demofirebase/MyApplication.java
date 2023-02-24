package com.jaroid.android51_demofirebase;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

public class MyApplication extends Application {

    public static final String CHANNEL_MESSAGE = "CHANNEL_MESSAGE";

    @Override
    public void onCreate() {
        super.onCreate();
        this.createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_MESSAGE, "message_notification", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("This is message channel");
            NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
