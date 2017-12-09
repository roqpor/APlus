package com.aplus.pillreminder.activity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.TimePicker;

import com.aplus.pillreminder.R;
import com.aplus.pillreminder.fragment.AddPillFragment;
import com.aplus.pillreminder.fragment.TimePickerFragment;
import com.aplus.pillreminder.model.RemindTime;

public class PillInfoActivity extends AppCompatActivity implements AddPillFragment.AddPillFragmentListener, TimePickerDialog.OnTimeSetListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pill_info);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragmentContainer, new AddPillFragment(), AddPillFragment.TAG)
                .commit();
        }
    }

    @Override
    public void onImgBtnAddTimePressed() {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    @Override
    public void onBtnOkPressed() {
        finish();
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
