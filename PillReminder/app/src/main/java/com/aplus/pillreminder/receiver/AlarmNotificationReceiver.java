package com.aplus.pillreminder.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.aplus.pillreminder.GlobalVariable;
import com.aplus.pillreminder.R;
import com.aplus.pillreminder.database.DatabaseManager;
import com.aplus.pillreminder.database.PillReminderDb;
import com.aplus.pillreminder.model.Pill;

import static android.app.Notification.VISIBILITY_PUBLIC;

public class AlarmNotificationReceiver extends BroadcastReceiver {

    public static final String KEY_PILL_ID = "pill_id";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(final Context context, Intent intent) {
        final NotificationManager notificationManager =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        final int uniqueId = intent.getIntExtra("id", -1);
        int pillId = intent.getIntExtra(KEY_PILL_ID, 0);
        final int hour = intent.getIntExtra("hour", 0);
        final int minute = intent.getIntExtra("minute", 0);

        final PillReminderDb db = DatabaseManager.getInstance().getDb();

        new AsyncTask<Integer, Void, Pill>() {
            @Override
            protected Pill doInBackground(Integer... integers) {
                return db.pillDao().loadPill(integers[0]);
            }

            @Override
            protected void onPostExecute(Pill pill) {
                Intent yesReceive = new Intent(context, TakeReceiver.class);
                yesReceive.putExtra("uniqueId", uniqueId);
                yesReceive.putExtra("pill", pill);
                yesReceive.putExtra("hour", hour);
                yesReceive.putExtra("minute", minute);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                        uniqueId,
                        yesReceive,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                Notification notification = new NotificationCompat.Builder(context, String.valueOf(uniqueId))
                        .setSmallIcon(R.drawable.ic_pill)
                        .addAction(R.drawable.ic_verify_16dp, "Take.", pendingIntent)
                        .setContentTitle(pill.getName())
                        .setContentText(pill.getDescribe())
                        .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                        .setVisibility(VISIBILITY_PUBLIC)
                        .setFullScreenIntent(pendingIntent, true)
                        .build();

                if(GlobalVariable.getIsEnabled(context)) {

                    try {
                        Uri notice = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        Ringtone r = RingtoneManager.getRingtone(context.getApplicationContext(), notice);
                        r.play();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    notificationManager.notify(uniqueId, notification);
                }
            }
        }.execute(pillId);
    }
}
