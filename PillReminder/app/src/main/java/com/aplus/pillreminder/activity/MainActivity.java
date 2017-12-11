package com.aplus.pillreminder.activity;

import android.app.Activity;
import android.content.Intent;
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
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, PillBagFragment.OnSwipeMenuClickListener {

    @BindView(R.id.navigation) BottomNavigationViewEx navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String currentFragmentTag = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer).getTag();
                if (HomeFragment.TAG.equals(currentFragmentTag)) {
                    HomeFragment fragment = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
                    fragment.loadPillWithRemindTime();
                } else if(ReportFragment.TAG.equals(currentFragmentTag)){
                    PillBagFragment fragment = (PillBagFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
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
}
