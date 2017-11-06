package com.aplus.pillreminder.activity;

import android.app.TimePickerDialog;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TimePicker;

import com.aplus.pillreminder.R;
import com.aplus.pillreminder.database.RemindTime;
import com.aplus.pillreminder.fragment.AddPillFragment;
import com.aplus.pillreminder.fragment.HomeFragment;
import com.aplus.pillreminder.fragment.TimePickerFragment;

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
    public void onBtnAddPress() {
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragmentContainer, new AddPillFragment(), AddPillFragment.TAG)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onImgBtnAddTimePressed() {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        AddPillFragment addPillFragment = (AddPillFragment) getSupportFragmentManager().findFragmentByTag(AddPillFragment.TAG);
        addPillFragment.addTime(new RemindTime(hourOfDay, minute));
    }
}
