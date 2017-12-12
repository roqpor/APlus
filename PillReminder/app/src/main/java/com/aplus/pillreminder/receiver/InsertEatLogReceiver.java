package com.aplus.pillreminder.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.aplus.pillreminder.database.DatabaseManager;
import com.aplus.pillreminder.database.PillReminderDb;
import com.aplus.pillreminder.model.EatLog;
import com.aplus.pillreminder.model.Pill;
import com.aplus.pillreminder.model.PillWithRemindTime;
import com.aplus.pillreminder.model.RemindTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InsertEatLogReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final PillReminderDb db = DatabaseManager.getInstance().getDb();

        new AsyncTask<Void, Void, List<PillWithRemindTime>>() {
            @Override
            protected List<PillWithRemindTime> doInBackground(Void... voids) {
                return db.pillDao().loadPillsWithRemindTimes();
            }

            @Override
            protected void onPostExecute(List<PillWithRemindTime> pillWithRemindTimes) {
                List<EatLog> eatLogs = new ArrayList<>();

                for (PillWithRemindTime pillWithRemindTime : pillWithRemindTimes) {
                    Pill pill = pillWithRemindTime.getPill();
                    for (RemindTime remindTime : pillWithRemindTime.getRemindTimeList()) {
                        Date date = new Date();
                        date.setHours(remindTime.getHour());
                        date.setMinutes(remindTime.getMinute());
                        date.setSeconds(0);

                        EatLog eatLog = new EatLog();
                        eatLog.setDate(date);
                        eatLog.setTaken(false);
                        eatLog.setPillName(pill.getName());
                        eatLog.setDose(pill.getDose());

                        eatLogs.add(eatLog);
                    }
                }

                new AsyncTask<List<EatLog>, Void, Void>() {
                    @Override
                    protected Void doInBackground(List<EatLog>[] lists) {
                        db.eatLogDao().insert(lists[0]);
                        return null;
                    }
                }.execute(eatLogs);
            }
        }.execute();
    }
}
