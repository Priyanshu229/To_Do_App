package com.example.todo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        createNotification(context, "Reminder", "You have to do ");
    }

    private void createNotification(Context context, String title, String message) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel_id", "Channel Name", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }


        Notification.Builder builder = new Notification.Builder(context, "channel_id")
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_reminder);

        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.notify(0, builder.build());
    }
}
