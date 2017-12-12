package com.aplus.pillreminder.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.aplus.pillreminder.R;
import com.aplus.pillreminder.controller.activity.MainActivity;
import com.aplus.pillreminder.database.DatabaseManager;
import com.aplus.pillreminder.database.PillReminderDb;
import com.aplus.pillreminder.model.EatLog;
import com.aplus.pillreminder.model.Pill;

import java.util.Calendar;
import java.util.Date;

import static android.app.Notification.VISIBILITY_PUBLIC;

public class TakeReceiver extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(final Context context, Intent intent) {
        final NotificationManager notificationManager =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        final int id = (int)SystemClock.currentThreadTimeMillis();

        Intent mainActivity = new Intent(context, MainActivity.class);
        mainActivity.putExtra("uniqueId", id);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                id,
                mainActivity,
                PendingIntent.FLAG_UPDATE_CURRENT);

        final PillReminderDb db = DatabaseManager.getInstance().getDb();

        int uniqueId = intent.getIntExtra("uniqueId", 0);
        final Pill pill = intent.getParcelableExtra("pill");
        final int hour = intent.getIntExtra("hour", 0);
        final int minute = intent.getIntExtra("minute", 0);
        notificationManager.cancel(uniqueId);

        final Notification notification = new NotificationCompat.Builder(context,
                String.valueOf(SystemClock.currentThreadTimeMillis()))
                .setSmallIcon(R.drawable.ic_warning_32dp)
                .setContentTitle("Warning")
                .setContentText(pill.getName().concat(" is not enough to take."))
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setVisibility(VISIBILITY_PUBLIC)
                .setFullScreenIntent(pendingIntent, true)
                .build();

        new AsyncTask<Void, Void, EatLog>() {

            @Override
            protected void onPreExecute() {
            }

            @Override
            protected EatLog doInBackground(Void... voids) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                Date date = calendar.getTime();

                return db.eatLogDao().getNotTakenLog(pill.getId(), date);
            }

            @Override
            protected void onPostExecute(EatLog eatLog) {
                int quantity = pill.getQuantity();
                int dose = pill.getDose();
                if (quantity < dose) {
                    notificationManager.notify(id, notification);
                } else {
                    pill.setQuantity(quantity - dose);
                    new AsyncTask<Pill, Void, Void>() {
                        @Override
                        protected Void doInBackground(Pill... pills) {
                            db.pillDao().update(pills[0]);
                            return null;
                        }
                    }.execute(pill);

                    new AsyncTask<EatLog, Void, Void>() {
                        @Override
                        protected Void doInBackground(EatLog... eatLogs) {
                            eatLogs[0].setTaken(true);
                            db.eatLogDao().update(eatLogs[0]);
                            return null;
                        }
                    }.execute(eatLog);
                }
            }
        }.execute();
    }
}
