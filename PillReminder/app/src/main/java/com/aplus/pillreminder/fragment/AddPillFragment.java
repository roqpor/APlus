package com.aplus.pillreminder.fragment;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.aplus.pillreminder.activity.MainActivity;
import com.aplus.pillreminder.model.EatLog;
import com.aplus.pillreminder.model.Pill;
import com.aplus.pillreminder.model.RemindTime;
import com.aplus.pillreminder.receiver.AlarmNotificationReceiver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddPillFragment extends PillInfoFragment {

    private Pill pill;

    public AddPillFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pill = new Pill();
    }


    @Override
    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
        super.onClick(dialog, selectedColor, allColors);
        pill.setColor(selectedColor);
    }

    @Override
    public void onActionConfirm() {
        if (validate()) {
            insertPill();
        }
    }

    private boolean validate() {
        pill.setName(actvName.getText().toString());
        pill.setDescribe(etDescribe.getText().toString());
        try {
            pill.setQuantity(Integer.parseInt(etQuantity.getText().toString()));
            pill.setDose(Integer.parseInt(etDose.getText().toString()));
        } catch (NumberFormatException e) {
            Toast.makeText(getActivity(), "Please enter quantity.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void insertPill() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                int id = (int) db.pillDao().insert(pill);
                pill.setId(id);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                insertEatLogs();
                insertRemindTimes();
            }
        }.execute();
    }

    private void insertEatLogs() {
        List<EatLog> eatLogs = new ArrayList<>();

        for (RemindTime remindTime : timeList) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, remindTime.getHour());
            calendar.set(Calendar.MINUTE, remindTime.getMinute());
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            Date date = calendar.getTime();

            EatLog eatLog = new EatLog();
            eatLog.setDate(date);
            eatLog.setTaken(false);
            eatLog.setPillId(pill.getId());
            eatLog.setPillName(pill.getName());
            eatLog.setDose(pill.getDose());

            eatLogs.add(eatLog);
        }

        new AsyncTask<List<EatLog>, Void, Void>() {
            @Override
            protected Void doInBackground(List<EatLog>[] lists) {
                db.eatLogDao().insert(lists[0]);
                return null;
            }
        }.execute(eatLogs);
    }

    private void insertRemindTimes() {
        new AsyncTask<List<RemindTime>, Void, Void>() {
            @Override
            protected Void doInBackground(List<RemindTime>[] lists) {
                for (RemindTime remindTime : lists[0]) {
                    remindTime.setPillId(pill.getId());
                    int id = (int) db.remindTimeDao().insert(remindTime);
                    setAlarm(id, remindTime.getHour(), remindTime.getMinute(), true, pill);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                listener.onActionConfirmCompleted();
            }
        }.execute(timeList);
    }

    private void setAlarm(int uniqueId, int hour, int minute, boolean isRepeat, Pill pill){
        AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);

//        int importance = NotificationManager.IMPORTANCE_HIGH;
//        NotificationChannel mChannel = new NotificationChannel(String.valueOf(uniqueId), "testtttt", importance);
//        mChannel.setDescription("Welcome to Notification Service");
//        mChannel.enableLights(true);
//        mChannel.enableVibration(true);
//        mChannel.setShowBadge(true);
//        mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

        Intent myIntent = new Intent(getActivity(), AlarmNotificationReceiver.class);
//        myIntent.putExtra("notificationChannel", mChannel);
        myIntent.putExtra("id", uniqueId);
        myIntent.putExtra(AlarmNotificationReceiver.KEY_PILL_ID, pill.getId());
        myIntent.putExtra("hour", hour);
        myIntent.putExtra("minute", minute);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), uniqueId, myIntent, 0);

        //cancel alarm
//        myIntent = new Intent(SetActivity.this, AlarmActivity.class);
//        pendingIntent = PendingIntent.getActivity(CellManageAddShowActivity.this,
//                id, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        pendingIntent.cancel();
//        alarmManager.cancel(pendingIntent);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        if(!isRepeat){
            assert alarmManager != null;
            alarmManager.set(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    pendingIntent);
        } else {
            assert alarmManager != null;
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    1000*60*60*24,
                    pendingIntent);
        }
    }
}
