package no.hiof.andrekar.badhabits;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import androidx.annotation.Nullable;
import model.DateHabit;
import model.EconomicHabit;
import model.Habit;

import static android.app.NotificationChannel.DEFAULT_CHANNEL_ID;


public class MyService extends Service {

    Intent intent;


    public MyService() {
        super();
    }

    @Nullable
    @android.support.annotation.Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public ComponentName startForegroundService(Intent service) {
        return super.startForegroundService(service);
    }

    @Override
    public int onStartCommand(Intent workIntent, int flags, int startId) {
            intent = workIntent;
        try {



            EconomicHabit eh;
            DateHabit dh;


            for (Habit h : Habit.habits) {
                if (h.getClass() == EconomicHabit.class) {
                    eh = (EconomicHabit) h;
                    if (eh.getProgress() <= 0) {
                        String dataString = workIntent.getDataString();

                        Notification notification = new
                                NotificationCompat.Builder(this, GlobalConstants.CHANNEL_ID)
                                .setSmallIcon(R.drawable.ic_coin)
                                .setContentTitle(h.getTitle())
                                .setContentText("Congratulations!")
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .build();

                        NotificationManager mNotificationManager =
                                (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);

                        mNotificationManager.notify(1, notification);


                    }
                } else if (h.getClass() == DateHabit.class) {
                    dh = (DateHabit) h;
                }
            }



            System.out.println("NOTIFICATION");
            Thread.sleep(5000);

            startService(workIntent);


        } catch (InterruptedException e) {
            // Restore interrupt status.
            Thread.currentThread().interrupt();
        }
        return super.onStartCommand(workIntent, flags, startId);
    }


    protected void onHandleIntent(Intent workIntent) {
        // Normally we would do some work here, like download a file.
        // For our sample, we just sleep for 5 seconds.
        try {
            intent = workIntent;

            String dataString = workIntent.getDataString();

            Notification notification = new
                    NotificationCompat.Builder(this, GlobalConstants.CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_coin)
                    .setContentTitle("textTitle")
                    .setContentText("textContent")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .build();

            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);

            mNotificationManager.notify(1, notification);
            /*

            EconomicHabit eh;
            DateHabit dh;


            for (Habit h : Habit.habits) {
                if (h.getClass() == EconomicHabit.class) {
                    eh = (EconomicHabit) h;
                    if (eh.getProgress() >= 0) {


                    }
                } else if (h.getClass() == DateHabit.class) {
                    dh = (DateHabit) h;
                }
            }

            */

            System.out.println("NOTIFICATION");
            Thread.sleep(5000);

            startService(intent);


        } catch (InterruptedException e) {
            // Restore interrupt status.
            Thread.currentThread().interrupt();
        }
    }
}


