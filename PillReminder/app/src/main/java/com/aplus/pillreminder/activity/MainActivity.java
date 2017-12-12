package com.aplus.pillreminder.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.aplus.pillreminder.R;
import com.aplus.pillreminder.database.DatabaseManager;
import com.aplus.pillreminder.fragment.HomeFragment;
import com.aplus.pillreminder.fragment.PillBagFragment;
import com.aplus.pillreminder.fragment.ReportFragment;
import com.aplus.pillreminder.model.PillWithRemindTime;
import com.aplus.pillreminder.receiver.InsertEatLogReceiver;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, ReportFragment.ReportFragmentListener, PillBagFragment.OnSwipeMenuClickListener {

    @BindView(R.id.navigation) BottomNavigationViewEx navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isFirstRun()) {
            setInsertEatLogSchedule();
        }

        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            DatabaseManager.getInstance().initDb(getApplicationContext());
            setTitle(R.string.app_name);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragmentContainer, new HomeFragment(), HomeFragment.TAG)
                    .commit();
        }

        setupBottomNavigation();
    }

    private boolean isFirstRun() {
        final String PREFS_NAME = "com.aplus.pillreminder";
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if (prefs.getBoolean("firstrun", true)) {
            prefs.edit().putBoolean("firstrun", false).apply();
            return true;
        } else {
            return false;
        }
    }

    private void setInsertEatLogSchedule() {
        Intent intent = new Intent(getApplicationContext(), InsertEatLogReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 60 * 24, pendingIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String currentFragmentTag = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer).getTag();
                if (HomeFragment.TAG.equals(currentFragmentTag)) {
                    HomeFragment fragment = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
                    fragment.loadPillWithRemindTime();
                } else if(ReportFragment.TAG.equals(currentFragmentTag)){
                    ReportFragment fragment = (ReportFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
                    // TODO
                } else if (PillBagFragment.TAG.equals(currentFragmentTag)) {
                    PillBagFragment fragment = (PillBagFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
                    fragment.loadPills();
                }
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                setTitle(R.string.app_name);
                onNavigationHome();
                break;
            case R.id.navigation_report:
                setTitle(R.string.title_report);
                onNavigationReport();
                break;
            case R.id.navigation_bag:
                setTitle(R.string.title_bag);
                onNavigationBag();
                break;
            case R.id.navigation_setting:
                setTitle(R.string.title_setting);
                break;
            case R.id.navigation_empty:
                return false;
        }
        return true;
    }

    @Override
    public void onEditClicked(PillWithRemindTime pillWithRemindTime) {
        Intent intent = new Intent(MainActivity.this, PillInfoActivity.class);
        intent.putExtra(PillInfoActivity.KEY_ACTION, PillInfoActivity.ACTION_UPDATE);
        intent.putExtra(PillInfoActivity.KEY_PILL_WITH_REMIND_TIME, pillWithRemindTime);
        startActivityForResult(intent, 1);
    }

    @OnClick(R.id.fab) public void onFab() {
        Intent intent = new Intent(MainActivity.this, PillInfoActivity.class);
        intent.putExtra(PillInfoActivity.KEY_ACTION, PillInfoActivity.ACTION_INSERT);
        startActivityForResult(intent, 1);
    }

    private void setupBottomNavigation() {
        navigation.enableItemShiftingMode(false);
        navigation.enableShiftingMode(false);
        navigation.enableAnimation(false);
        navigation.setTextVisibility(false);
        navigation.setOnNavigationItemSelectedListener(this);
    }

    public void onNavigationHome() {
        if (isCurrentFragment(HomeFragment.TAG)) {
            return;
        }
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.fragmentContainer, new HomeFragment(), HomeFragment.TAG)
                .commit();
    }

    public void onNavigationReport() {
        if (isCurrentFragment(ReportFragment.TAG)) {
            return;
        }
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.fragmentContainer, new ReportFragment(), ReportFragment.TAG)
                .commit();
    }

    public void onNavigationBag() {
        if (isCurrentFragment(PillBagFragment.TAG)) {
            return;
        }
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.fragmentContainer, new PillBagFragment(), PillBagFragment.TAG)
                .commit();
    }

    private boolean isCurrentFragment(String tag) {
        String currentFragmentTag = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer).getTag();
        return tag.equals(currentFragmentTag);
    }

    @Override
    public void onDateItemClicked(Date date) {
        Intent intent = new Intent(MainActivity.this, ShowEatLogActivity.class);
        startActivity(intent);
    }
}
