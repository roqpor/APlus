package com.aplus.pillreminder.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.aplus.pillreminder.database.DatabaseManager;
import com.aplus.pillreminder.database.PillReminderDb;
import com.aplus.pillreminder.model.EatLog;
import com.aplus.pillreminder.model.Pill;

import java.util.Calendar;
import java.util.Date;

public class YesReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Toast", Toast.LENGTH_SHORT).show();
        final PillReminderDb db = DatabaseManager.getInstance().getDb();

        final Pill pill = intent.getParcelableExtra("pill");
        final int hour = intent.getIntExtra("hour", 0);
        final int minute = intent.getIntExtra("minute", 0);

        new AsyncTask<Void, Void, EatLog>() {
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
                new AsyncTask<EatLog, Void, Void>() {
                    @Override
                    protected Void doInBackground(EatLog... eatLogs) {
                        eatLogs[0].setTaken(true);
                        db.eatLogDao().update(eatLogs[0]);
                        return null;
                    }
                }.execute(eatLog);
            }
        }.execute();
    }
}
