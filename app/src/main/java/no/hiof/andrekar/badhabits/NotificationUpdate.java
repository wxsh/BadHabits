package no.hiof.andrekar.badhabits;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;

public class NotificationUpdate extends BroadcastReceiver {
    Bundle extras;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        //NEED A RESCHEDULE?

        extras = intent.getExtras();

        updateNotification(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateNotification(Context context){
        Notification notification = new Notification.Builder(context.getApplicationContext(), GlobalConstants.CHANNEL_ID)
                .setContentTitle(extras.getString("habitName"))
                .setContentText("Congratulations, you reached your goal!")
                .setSmallIcon(R.drawable.ic_calendar_today)
                .setAutoCancel(true)
                .build();
        NotificationManager manager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(manager!=null) manager.notify(123, notification);
    }
}