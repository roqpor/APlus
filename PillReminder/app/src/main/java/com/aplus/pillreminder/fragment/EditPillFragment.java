package com.aplus.pillreminder.fragment;


import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aplus.pillreminder.model.Pill;
import com.aplus.pillreminder.model.PillWithRemindTime;
import com.aplus.pillreminder.model.RemindTime;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditPillFragment extends PillInfoFragment {

    public static final String KEY_PILL_WITH_REMIND_TIME = "pillInfoWithRemindTime";
    private PillWithRemindTime pillWithRemindTime;

    public EditPillFragment() {
        // Required empty public constructor
    }

    public EditPillFragment newInstance(PillWithRemindTime pillWithRemindTime) {
        Bundle args = new Bundle();
        args.putParcelable(KEY_PILL_WITH_REMIND_TIME, pillWithRemindTime);
        EditPillFragment fragment = new EditPillFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pillWithRemindTime = getArguments().getParcelable(KEY_PILL_WITH_REMIND_TIME);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        setPillDetails();
        return view;
    }

    @Override
    public void onActionConfirm() {
        updatePill();
        deleteOldRemindTimes();
        insertNewRemindTimes();
    }

    @Override
    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
        super.onClick(dialog, selectedColor, allColors);
        pillWithRemindTime.getPill().setColor(selectedColor);
    }

    @Override
    public void addTime(RemindTime remindTime) {
        super.addTime(remindTime);
        remindTime.setPillId(pillWithRemindTime.getPill().getId());
    }

    private void setPillDetails() {
        Pill pill = pillWithRemindTime.getPill();

        imgPill.setColorFilter(pill.getColor());
        actvName.setText(pill.getName());
        etDescribe.setText(pill.getDescribe());
        etQuantity.setText(String.valueOf(pill.getQuantity()));
        etDose.setText(String.valueOf(pill.getDose()));

        for (RemindTime r : pillWithRemindTime.getRemindTimeList()) {
            addTime(r);
        }
    }

    private void updatePill() {
        Pill pill = pillWithRemindTime.getPill();
        pill.setName(actvName.getText().toString());
        pill.setDescribe(etDescribe.getText().toString());
        try {
            pill.setQuantity(Integer.parseInt(etQuantity.getText().toString()));
            pill.setDose(Integer.parseInt(etDose.getText().toString()));
        } catch (NumberFormatException e) {
            Toast.makeText(getActivity(), "Please enter quantity.", Toast.LENGTH_LONG).show();
            return;
        }

        new AsyncTask<Pill, Void, Void>() {
            @Override
            protected Void doInBackground(Pill... pills) {
                db.pillDao().update(pills[0]);
                return null;
            }
        }.execute(pill);
    }

    private void deleteOldRemindTimes() {
        new AsyncTask<List<RemindTime>, Void, Void>() {
            @Override
            protected Void doInBackground(List<RemindTime>[] lists) {
                for (RemindTime r : lists[0]) {
                    db.remindTimeDao().delete(r);
                }
                return null;
            }
        }.execute(pillWithRemindTime.getRemindTimeList());
    }

    private void insertNewRemindTimes() {
        new AsyncTask<List<RemindTime>, Void, Void>() {
            @Override
            protected Void doInBackground(List<RemindTime>[] lists) {
                for (RemindTime r : lists[0]) {
                    db.remindTimeDao().insert(r);
                }
                return null;
            }
        }.execute(timeList);
    }
}
