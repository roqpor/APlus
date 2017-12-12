package com.aplus.pillreminder.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.aplus.pillreminder.database.DatabaseManager;
import com.aplus.pillreminder.database.PillReminderDb;
import com.aplus.pillreminder.model.EatLog;
import com.aplus.pillreminder.model.Pill;
import com.aplus.pillreminder.model.PillWithRemindTime;
import com.aplus.pillreminder.model.RemindTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class YesReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Toast", Toast.LENGTH_SHORT).show();
        final PillReminderDb db = DatabaseManager.getInstance().getDb();

        final Pill pill = intent.getParcelableExtra("pill");
        final int hour = intent.getIntExtra("hour", 0);
        final int minute = intent.getIntExtra("minute", 0);

        new AsyncTask<Void, Void, List<EatLog>>() {
            @Override
            protected List<EatLog> doInBackground(Void... voids) {
                Date date = new Date();
                date.setHours(hour);
                date.setMinutes(minute);
                date.setSeconds(0);

                Log.wtf("time", hour + ", " + minute);
                return db.eatLogDao().getNotTakenLogs(pill.getId(), date, date);
            }

            @Override
            protected void onPostExecute(List<EatLog> eatLogs) {

            }
        }.execute();
    }
}
