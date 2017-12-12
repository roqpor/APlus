package com.aplus.pillreminder.controller.fragment;


import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.aplus.pillreminder.R;
import com.aplus.pillreminder.database.DatabaseManager;
import com.aplus.pillreminder.database.PillReminderDb;
import com.aplus.pillreminder.model.RemindTime;
import com.aplus.pillreminder.model.StaticData;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class PillInfoFragment extends Fragment implements SwipeMenuListView.OnMenuItemClickListener, ColorPickerClickListener {

    protected PillReminderDb db;
    @BindView(R.id.imgPill) ImageView imgPill;
    @BindView(R.id.actvName) AutoCompleteTextView actvName;
    @BindView(R.id.etDescribe) EditText etDescribe;
    @BindView(R.id.etQuantity) EditText etQuantity;
    @BindView(R.id.etDose) EditText etDose;
    @BindView(R.id.imgBtnAddTime) ImageButton imgBtnAddTime;
    @BindView(R.id.listView) SwipeMenuListView listView;
    protected List<RemindTime> timeList;
    protected ArrayAdapter<RemindTime> listViewAdapter;
    protected ArrayAdapter<String> actvNameAdapter;
    protected PillInfoFragmentListener listener;

    public interface PillInfoFragmentListener {
        void onImgBtnAddTimePressed();
        void onActionConfirmCompleted();
    }

    public PillInfoFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        db = DatabaseManager.getInstance().getDb();
        timeList = new ArrayList<>();
        listViewAdapter = new ArrayAdapter<>(getActivity(), R.layout.item_remind_time_list, timeList);
        actvNameAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, StaticData.PILL_NAMES);
        listener = (PillInfoFragmentListener) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_pill, container, false);
        ButterKnife.bind(this, view);
        setup();

        return view;
    }

    @OnClick(R.id.imgPill) void onImgPill() {
        ColorPickerDialogBuilder
                .with(getActivity())
                .setTitle("Choose color")
                .initialColor(Color.WHITE)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setPositiveButton("OK", this)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }

    @OnClick(R.id.imgBtnAddTime) void onImgBtnAddTime() {
        listener.onImgBtnAddTimePressed();
    }

    @Override
    public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
        RemindTime remindTime = timeList.get(position);

        switch (index) {
            case 0:
                deleteTime(remindTime);
                break;
        }
        // false : close the menu; true : not close the menu
        return true;
    }

    @Override
    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
        imgPill.setColorFilter(selectedColor);
    }

    abstract public void onActionConfirm();

    private void setup() {
        actvName.setAdapter(actvNameAdapter);

        listView.setAdapter(listViewAdapter);
        createSwipeMenu();

        listener = (PillInfoFragmentListener) getActivity();
    }

    private void createSwipeMenu() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity());
                deleteItem.setBackground(android.R.color.holo_red_light);
                deleteItem.setWidth(dp2px(90));
                deleteItem.setIcon(R.drawable.ic_delete);
                menu.addMenuItem(deleteItem);
            }
        };

        // set creator
        listView.setMenuCreator(creator);
        listView.setOnMenuItemClickListener(this);
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    public void addTime(RemindTime remindTime) {
        int totalMinute = remindTime.getTotalMinute();

        for (RemindTime r : timeList) {
            if (r.getTotalMinute() == totalMinute) {
                return;
            }
        }

        timeList.add(remindTime);

        Collections.sort(timeList, new Comparator<RemindTime>() {
            @Override
            public int compare(RemindTime t1, RemindTime t2) {
                return t1.getTotalMinute() - t2.getTotalMinute();
            }
        });

        listView.invalidateViews();
    }

    private void deleteTime(RemindTime remindTime) {
        timeList.remove(remindTime);
        listView.invalidateViews();
    }
}
