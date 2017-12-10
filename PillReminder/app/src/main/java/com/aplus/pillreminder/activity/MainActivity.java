package com.aplus.pillreminder.activity;

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
                    .add(R.id.fragmentContainer, new HomeFragment())
                    .commit();
        }

        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        navigation.enableItemShiftingMode(false);
        navigation.enableShiftingMode(false);
        navigation.enableAnimation(false);
        navigation.setTextVisibility(false);
        navigation.setOnNavigationItemSelectedListener(this);
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

    @OnClick(R.id.fab) public void onFab() {
        Intent intent = new Intent(MainActivity.this, PillInfoActivity.class);
        intent.putExtra(PillInfoActivity.KEY_ACTION, PillInfoActivity.ACTION_INSERT);
        startActivity(intent);
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
    public void onEditClicked(PillWithRemindTime pillWithRemindTime) {
        Intent intent = new Intent(MainActivity.this, PillInfoActivity.class);
        intent.putExtra(PillInfoActivity.KEY_ACTION, PillInfoActivity.ACTION_UPDATE);
        intent.putExtra(PillInfoActivity.KEY_PILL_WITH_REMIND_TIME, pillWithRemindTime);
        startActivity(intent);
    }
}
