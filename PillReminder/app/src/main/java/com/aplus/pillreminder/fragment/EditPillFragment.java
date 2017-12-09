package com.aplus.pillreminder.fragment;


import android.content.DialogInterface;
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
    void onBtnOk() {
        Toast.makeText(getActivity(), "onBtnOk", Toast.LENGTH_SHORT).show();
//        listener.onBtnOkPressed();
    }

    @Override
    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
        super.onClick(dialog, selectedColor, allColors);
        pillWithRemindTime.getPill().setColor(selectedColor);
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
}
