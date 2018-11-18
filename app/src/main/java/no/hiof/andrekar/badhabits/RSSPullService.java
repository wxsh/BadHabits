package no.hiof.andrekar.badhabits;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import model.DateHabit;
import model.EconomicHabit;
import model.Habit;

import static android.app.NotificationChannel.DEFAULT_CHANNEL_ID;


public class RSSPullService extends IntentService {

    public RSSPullService() {
        super("DisplayNotification");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Normally we would do some work here, like download a file.
        // For our sample, we just sleep for 5 seconds.


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

        EconomicHabit eh;
        DateHabit dh;
        try {
            for (Habit h : Habit.habits) {
                if(h.getClass() == EconomicHabit.class){
                    eh = (EconomicHabit)h;
                    if (eh.getProgress() >=0) {



                    }
                }
                else if(h.getClass() == DateHabit.class){
                    dh = (DateHabit)h;
                }
            }


            System.out.println("NOTIFICATION");
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // Restore interrupt status.
            Thread.currentThread().interrupt();
        }
    }
}
