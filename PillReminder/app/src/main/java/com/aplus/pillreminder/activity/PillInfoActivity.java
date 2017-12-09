package com.aplus.pillreminder.activity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.TimePicker;

import com.aplus.pillreminder.R;
import com.aplus.pillreminder.fragment.AddPillFragment;
import com.aplus.pillreminder.fragment.EditPillFragment;
import com.aplus.pillreminder.fragment.PillInfoFragment;
import com.aplus.pillreminder.fragment.TimePickerFragment;
import com.aplus.pillreminder.model.PillWithRemindTime;
import com.aplus.pillreminder.model.RemindTime;

public class PillInfoActivity extends AppCompatActivity implements PillInfoFragment.PillInfoFragmentListener, TimePickerDialog.OnTimeSetListener {

    public static final String KEY_ACTION = "action";
    public static final String KEY_PILL_WITH_REMIND_TIME = "pillInfoWithRemindTime";
    public static final int ACTION_INSERT = 1;
    public static final int ACTION_UPDATE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pill_info);

        if (savedInstanceState == null) {
            int action = getIntent().getIntExtra(KEY_ACTION, 0);

            if (action == ACTION_INSERT) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragmentContainer, new AddPillFragment())
                        .commit();
            } else if (action == ACTION_UPDATE) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragmentContainer, new EditPillFragment().newInstance((PillWithRemindTime) getIntent().getParcelableExtra(KEY_PILL_WITH_REMIND_TIME)))
                        .commit();
            }
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
        PillInfoFragment fragment = (PillInfoFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);

        RemindTime remindTime = new RemindTime();
        remindTime.setHour(hourOfDay);
        remindTime.setMinute(minute);

        fragment.addTime(remindTime);
    }
}
