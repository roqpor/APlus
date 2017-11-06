package com.aplus.pillreminder.activity;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.aplus.pillreminder.R;
import com.aplus.pillreminder.fragment.AddPillFragment;
import com.aplus.pillreminder.fragment.HomeFragment;

public class MainActivity extends AppCompatActivity implements HomeFragment.HomeFragmentListener {

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
    public void onBtnAddPress() {
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragmentContainer, new AddPillFragment())
                .addToBackStack(null)
                .commit();
    }
}
