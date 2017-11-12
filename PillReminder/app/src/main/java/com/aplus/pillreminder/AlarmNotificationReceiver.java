package com.aplus.pillreminder;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.aplus.pillreminder.activity.MainActivity;
import com.github.arturogutierrez.Badges;
import com.github.arturogutierrez.BadgesNotSupportedException;

import static android.app.Notification.BADGE_ICON_SMALL;
import static android.app.Notification.VISIBILITY_PRIVATE;
import static android.app.Notification.VISIBILITY_PUBLIC;

public class AlarmNotificationReceiver extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent mainActivity = new Intent(context, MainActivity.class);

        int uniqueId = intent.getIntExtra("id", -1);

        if(uniqueId == -1){
            Toast.makeText(context, "id equal -1", Toast.LENGTH_SHORT).show();
            uniqueId = (int) System.currentTimeMillis();
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(context, uniqueId, mainActivity, PendingIntent.FLAG_UPDATE_CURRENT);

//        assert notificationManager != null;
//        notificationManager.createNotificationChannel(mChannel);

//        int messageCount = 3;

        Notification notification = new NotificationCompat.Builder(context, String.valueOf(uniqueId))
                .setAutoCancel(false)
                .setSmallIcon(R.drawable.ic_pill)
                .setContentTitle("Time to eat drugs.")
                .setContentText("Welcome to Notification Service")
                .setContentIntent(pendingIntent)
                .setBadgeIconType(BADGE_ICON_SMALL)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setVisibility(VISIBILITY_PUBLIC)
                .setFullScreenIntent(pendingIntent, true)
//                .setCustomHeadsUpContentView();
//                .setNumber(messageCount)
                .build();

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
