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
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.aplus.pillreminder.R;
import com.aplus.pillreminder.model.Pill;

import static android.app.Notification.VISIBILITY_PUBLIC;

public class AlarmNotificationReceiver extends BroadcastReceiver {

    public static final String KEY_PILL = "pill";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        int uniqueId = intent.getIntExtra("id", -1);
        Pill pill = intent.getParcelableExtra(KEY_PILL);
        int hour = intent.getIntExtra("hour", 0);
        int minute = intent.getIntExtra("minute", 0);

        if(uniqueId == -1){
            Toast.makeText(context, "id equal -1", Toast.LENGTH_SHORT).show();
            uniqueId = (int) System.currentTimeMillis();
        }

        Intent yesReceive = new Intent(context, YesReceiver.class);
        yesReceive.putExtra("pill", pill);
        yesReceive.putExtra("hour", hour);
        yesReceive.putExtra("minute", minute);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                uniqueId,
                yesReceive,
                PendingIntent.FLAG_UPDATE_CURRENT);

//        assert notificationManager != null;
//        notificationManager.createNotificationChannel(mChannel);

//        int messageCount = 3;

        Notification notification = new NotificationCompat.Builder(context, String.valueOf(uniqueId))
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_pill)
                .addAction(R.drawable.ic_verify_16dp, "Yes", pendingIntent)
                .setContentTitle(pill.getName())
                .setContentText(pill.getDescribe())
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
