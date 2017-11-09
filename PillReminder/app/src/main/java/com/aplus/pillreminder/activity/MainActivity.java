package com.aplus.pillreminder.activity;

import android.app.TimePickerDialog;
import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.TimePicker;

import com.aplus.pillreminder.R;
import com.aplus.pillreminder.database.PillReminderDb;
import com.aplus.pillreminder.fragment.AddPillFragment;
import com.aplus.pillreminder.fragment.HomeFragment;
import com.aplus.pillreminder.fragment.PillBagFragment;
import com.aplus.pillreminder.fragment.TimePickerFragment;
import com.aplus.pillreminder.model.Pill;
import com.aplus.pillreminder.model.PillWithRemindTime;
import com.aplus.pillreminder.model.RemindTime;

import java.util.List;

public class MainActivity extends AppCompatActivity implements HomeFragment.HomeFragmentListener, AddPillFragment.AddPillFragmentListener, TimePickerDialog.OnTimeSetListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragmentContainer, new HomeFragment())
                    .commit();
        }
    }

    @Override
    public void onBtnAddPressed() {
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragmentContainer, new AddPillFragment(), AddPillFragment.TAG)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBtnBagPressed() {
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragmentContainer, new PillBagFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onImgBtnAddTimePressed() {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    @Override
    public void onBtnOkPressed() {
        // TODO: pill bag fragment
        // print Pill, RemindTime data in the db
        final PillReminderDb pillReminderDb = Room.databaseBuilder(getApplicationContext(),
                PillReminderDb.class, "PillReminder.db")
                .fallbackToDestructiveMigration()
                .build();

        new AsyncTask<Void, Void, List<PillWithRemindTime>>() {
            @Override
            protected List<PillWithRemindTime> doInBackground(Void... voids) {
                return pillReminderDb.pillDao().loadPillsWithRemindTimes();
            }

            @Override
            protected void onPostExecute(List<PillWithRemindTime> pillWithRemindTimes) {
                super.onPostExecute(pillWithRemindTimes);

                System.out.println("---------------------------------------------------------");

                for (PillWithRemindTime pillWithRemindTime : pillWithRemindTimes) {
                    Pill pill = pillWithRemindTime.getPill();

                    System.out.printf("[%d, %s, %s, %d, %d]\n",
                            pill.getId(), pill.getName(), pill.getDescribe(), pill.getQuantity(), pill.getDose());

                    for (RemindTime remindTime : pillWithRemindTime.getRemindTimeList()) {
                        System.out.printf("    - %d, %s\n",
                                remindTime.getId(), remindTime.toString());
                    }
                    System.out.println("---------------------------------------------------------");
                }
            }
        }.execute();

        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        AddPillFragment addPillFragment = (AddPillFragment) getSupportFragmentManager().findFragmentByTag(AddPillFragment.TAG);

        RemindTime remindTime = new RemindTime();
        remindTime.setHour(hourOfDay);
        remindTime.setMinute(minute);

        addPillFragment.addTime(remindTime);
    }
}
