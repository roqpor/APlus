package com.aplus.pillreminder.fragment;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.aplus.pillreminder.AlarmNotificationReceiver;
import com.aplus.pillreminder.R;
import com.aplus.pillreminder.database.DatabaseManager;
import com.aplus.pillreminder.database.PillReminderDb;
import com.aplus.pillreminder.model.Pill;
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
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddPillFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = AddPillFragment.class.getSimpleName();
    private PillReminderDb db;
    @BindView(R.id.imgPill) ImageView imgPill;
    @BindView(R.id.actvName) AutoCompleteTextView actvName;
    @BindView(R.id.etDescribe) EditText etDescribe;
    @BindView(R.id.etQuantity) EditText etQuantity;
    @BindView(R.id.etDose) EditText etDose;
    @BindView(R.id.btnOk) Button btnOk;
    @BindView(R.id.imgBtnAddTime) ImageButton imgBtnAddTime;
    @BindView(R.id.listView) SwipeMenuListView listView;
    private List<RemindTime> timeList;
    private ArrayAdapter<RemindTime> listViewAdapter;
    private ArrayAdapter<String> actvNameAdapter;
    private Pill pill;
    private AddPillFragmentListener listener;

    public interface AddPillFragmentListener {
        void onImgBtnAddTimePressed();
        void onBtnOkPressed();
    }

    public AddPillFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = DatabaseManager.getInstance().getDb();

        pill = new Pill();

        timeList = new ArrayList<>();
        listViewAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, timeList);

        actvNameAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, StaticData.PILL_NAMES);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_pill, container, false);

        ButterKnife.bind(this, view);

        setup(view);

        return view;
    }

    private void setup(View view) {
        actvName.setAdapter(actvNameAdapter);

        btnOk.setOnClickListener(this);

        imgBtnAddTime.setOnClickListener(this);

        listView.setAdapter(listViewAdapter);
        createSwipeMenu();

        listener = (AddPillFragmentListener) getActivity();
    }

    private void createSwipeMenu() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity());
                // set item background
                deleteItem.setBackground(android.R.color.holo_red_light);
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete_forever_black_24dp);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        // set creator
        listView.setMenuCreator(creator);

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
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
        });

        // Right
        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBtnAddTime:
                onImgBtnAddTime();
                break;

            case R.id.btnOk:
                onBtnOk();
                break;
        }
    }

    @OnClick(R.id.imgPill) void onImgPill() {
        ColorPickerDialogBuilder
                .with(getActivity())
                .setTitle("Choose color")
                .initialColor(Color.WHITE)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setPositiveButton("OK", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        imgPill.setColorFilter(selectedColor);
                        pill.setColor(selectedColor);
                    }
                })
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

    @OnClick(R.id.btnOk) void onBtnOk() {
        insertPillWithTimes();
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

    private void insertPillWithTimes() {
        pill.setName(actvName.getText().toString());
        pill.setDescribe(etDescribe.getText().toString());
        try {
            pill.setQuantity(Integer.parseInt(etQuantity.getText().toString()));
            pill.setDose(Integer.parseInt(etDose.getText().toString()));
        } catch (NumberFormatException e) {
            Toast.makeText(getActivity(), "Please enter the number.", Toast.LENGTH_LONG).show();
            return;
        }

        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                return db.pillDao().insert(pill);
            }

            @Override
            protected void onPostExecute(final Long aLong) {
                super.onPostExecute(aLong);

                for (final RemindTime remindTime : timeList) {
                    new AsyncTask<Void, Void, Long>() {
                        @Override
                        protected Long doInBackground(Void... voids) {
                            remindTime.setPillId(aLong.intValue());

                            return db.remindTimeDao().insert(remindTime);
                        }

                        @Override
                        protected void onPostExecute(Long aLong) {
//                            setAlarm(aLong.intValue(), remindTime.getHour(), remindTime.getMinute(), false, pill);
                            listener.onBtnOkPressed();
                        }
                    }.execute();
                }
            }
        }.execute();
    }

    private void setAlarm(int uniqueId, int hour, int minute, boolean isRepeat, Pill pill){

        AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);

//        int importance = NotificationManager.IMPORTANCE_HIGH;
//        NotificationChannel mChannel = new NotificationChannel(String.valueOf(uniqueId), "testtttt", importance);
//        mChannel.setDescription("Welcome to Notification Service");
//        mChannel.enableLights(true);
//        mChannel.enableVibration(true);
//        mChannel.setShowBadge(true);
//        mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

        Intent myIntent = new Intent(getActivity(), AlarmNotificationReceiver.class);
//        myIntent.putExtra("notificationChannel", mChannel);
        myIntent.putExtra("id", uniqueId);
        myIntent.putExtra("pill", pill);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), uniqueId, myIntent, 0);

        //cancel alarm
//        myIntent = new Intent(SetActivity.this, AlarmActivity.class);
//        pendingIntent = PendingIntent.getActivity(CellManageAddShowActivity.this,
//                id, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        pendingIntent.cancel();
//        alarmManager.cancel(pendingIntent);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        if(!isRepeat){
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),3000, pendingIntent);
        }
    }
}
