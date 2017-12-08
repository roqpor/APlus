package com.aplus.pillreminder.activity;

import android.app.TimePickerDialog;
import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
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
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, AddPillFragment.AddPillFragmentListener, TimePickerDialog.OnTimeSetListener {

    @BindView(R.id.navigation)
    BottomNavigationViewEx navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragmentContainer, new HomeFragment())
                    .commit();
        }

        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        navigation.enableItemShiftingMode(false);
        navigation.enableShiftingMode(false);
        navigation.enableAnimation(false);
        navigation.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                onNavigationHome();
                break;
            case R.id.navigation_report:
                break;
            case R.id.navigation_bag:
                onNavigationBag();
                break;
            case R.id.navigation_setting:
                break;
            case R.id.navigation_empty:
                return false;
        }
        return true;
    }

    @OnClick(R.id.fab)
    public void onFab() {
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.fragmentContainer, new AddPillFragment(), AddPillFragment.TAG)
                .commit();
    }

    public void onNavigationHome() {
        if (HomeFragment.TAG.equals(getSupportFragmentManager().findFragmentById(R.id.fragmentContainer).getTag())) {
            return;
        }
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.fragmentContainer, new HomeFragment(), HomeFragment.TAG)
                .commit();
    }

    public void onNavigationBag() {
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.fragmentContainer, new PillBagFragment())
                .commit();
    }

    @Override
    public void onImgBtnAddTimePressed() {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    @Override
    public void onBtnOkPressed() {
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
