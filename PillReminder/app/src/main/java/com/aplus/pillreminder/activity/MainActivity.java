package com.aplus.pillreminder.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.aplus.pillreminder.R;
import com.aplus.pillreminder.fragment.HomeFragment;

public class MainActivity extends AppCompatActivity {

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
}
