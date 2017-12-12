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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aplus.pillreminder.model.EatLog;
import com.aplus.pillreminder.model.Pill;
import com.aplus.pillreminder.model.PillWithRemindTime;
import com.aplus.pillreminder.model.RemindTime;
import com.aplus.pillreminder.receiver.AlarmNotificationReceiver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditPillFragment extends PillInfoFragment {

    public static final String KEY_PILL_WITH_REMIND_TIME = "pillInfoWithRemindTime";
    private PillWithRemindTime pillWithRemindTime;

    public EditPillFragment() {
        // Required empty public constructor
    }

    public EditPillFragment newInstance(PillWithRemindTime pillWithRemindTime) {
        Bundle args = new Bundle();
        args.putParcelable(KEY_PILL_WITH_REMIND_TIME, pillWithRemindTime);
        EditPillFragment fragment = new EditPillFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pillWithRemindTime = getArguments().getParcelable(KEY_PILL_WITH_REMIND_TIME);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        setPillDetails();
        return view;
    }

    @Override
    public void onActionConfirm() {
        if (validate()) {
            deleteAllNotTakenLogs();
            insertNewEatLogs();
            updatePill();
            deleteOldRemindTimes();
            cancelOldAlarms();
            insertNewRemindTimes();
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
        super.onClick(dialog, selectedColor, allColors);
        pillWithRemindTime.getPill().setColor(selectedColor);
    }

    @Override
    public void addTime(RemindTime remindTime) {
        super.addTime(remindTime);
        remindTime.setPillId(pillWithRemindTime.getPill().getId());
    }

    private void setPillDetails() {
        Pill pill = pillWithRemindTime.getPill();

        imgPill.setColorFilter(pill.getColor());
        actvName.setText(pill.getName());
        etDescribe.setText(pill.getDescribe());
        etQuantity.setText(String.valueOf(pill.getQuantity()));
        etDose.setText(String.valueOf(pill.getDose()));

        for (RemindTime r : pillWithRemindTime.getRemindTimeList()) {
            addTime(r);
        }
    }

    private boolean validate() {
        Pill pill = pillWithRemindTime.getPill();
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

    private void updatePill() {
        new AsyncTask<Pill, Void, Void>() {
            @Override
            protected Void doInBackground(Pill... pills) {
                db.pillDao().update(pills[0]);
                return null;
            }
        }.execute(pillWithRemindTime.getPill());
    }

    private void deleteOldRemindTimes() {
        new AsyncTask<List<RemindTime>, Void, Void>() {
            @Override
            protected Void doInBackground(List<RemindTime>[] lists) {
                for (RemindTime r : lists[0]) {
                    db.remindTimeDao().delete(r);
                }
                return null;
            }
        }.execute(pillWithRemindTime.getRemindTimeList());
    }

    private void insertNewRemindTimes() {
        new AsyncTask<List<RemindTime>, Void, Void>() {
            @Override
            protected Void doInBackground(List<RemindTime>[] lists) {
                for (RemindTime r : lists[0]) {
                    int id = (int) db.remindTimeDao().insert(r);
                    r.setId(id);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                setNewAlarms();
                listener.onActionConfirmCompleted();
            }
        }.execute(timeList);
    }

    private void deleteAllNotTakenLogs() {
        new AsyncTask<Void, Void, List<EatLog>>() {
            @Override
            protected List<EatLog> doInBackground(Void... voids) {
                Calendar calendar = Calendar.getInstance();

                // Date at 00:00.00
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                Date dayStart = calendar.getTime();

                // Date at 23:59.59
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);
                calendar.set(Calendar.MILLISECOND, 999);
                Date dayEnd = calendar.getTime();

                return db.eatLogDao().getNotTakenLogs(dayStart, dayEnd);
            }

            @Override
            protected void onPostExecute(List<EatLog> eatLogs) {
                new AsyncTask<List<EatLog>, Void, Void>() {
                    @Override
                    protected Void doInBackground(List<EatLog>[] lists) {
                        db.eatLogDao().delete(lists[0]);
                        return null;
                    }
                }.execute(eatLogs);
            }
        }.execute();
    }

    private void insertNewEatLogs() {
        List<EatLog> eatLogs = new ArrayList<>();
        Pill pill = pillWithRemindTime.getPill();

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

    private void cancelOldAlarms() {

        AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), AlarmNotificationReceiver.class);

        for (RemindTime r : pillWithRemindTime.getRemindTimeList()) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), r.getId(), intent, 0);
            alarmManager.cancel(pendingIntent);
        }
    }

    private void setNewAlarms() {
        for (RemindTime r : timeList) {
            setAlarm(r.getId(), r.getHour(), r.getMinute(), false, pillWithRemindTime.getPill());
        }
    }

    private void setAlarm(int uniqueId, int hour, int minute, boolean isRepeat, Pill pill){
        AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        Log.wtf("remindTime", hour + ", " + minute);

        Intent myIntent = new Intent(getActivity(), AlarmNotificationReceiver.class);
        myIntent.putExtra("id", uniqueId);
        myIntent.putExtra(AlarmNotificationReceiver.KEY_PILL, pill);
        myIntent.putExtra("hour", hour);
        myIntent.putExtra("minute", minute);


        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(),
                uniqueId,
                myIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

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
                    3000,
                    pendingIntent);
        }
    }
}
