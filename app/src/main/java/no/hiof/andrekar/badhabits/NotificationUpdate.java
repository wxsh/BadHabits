package no.hiof.andrekar.badhabits;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;

public class NotificationUpdate extends BroadcastReceiver {
    Bundle extras;
    Intent intent;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        extras = intent.getExtras();
        this.intent = intent;
        updateNotification(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateNotification(Context context){
        Intent resultIntent = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(context.getApplicationContext(), GlobalConstants.CHANNEL_ID)
                .setContentTitle(extras.getString("habitName"))
                .setContentText(context.getString((R.string.notification_reached_goal)))
                .setSmallIcon(R.drawable.ic_calendar_today)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .build();
        NotificationManager manager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(manager!=null) manager.notify(123, notification);
    }
}