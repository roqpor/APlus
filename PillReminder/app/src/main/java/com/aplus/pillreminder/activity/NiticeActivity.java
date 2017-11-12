package com.aplus.pillreminder.activity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aplus.pillreminder.AlarmNotificationReceiver;
import com.aplus.pillreminder.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class NiticeActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_display_clock;
    private SimpleDateFormat spdf;
    private Handler handler;
    private Runnable runnable;
    private Button btn_notification;
    private EditText et_hour, et_minute;
    private String mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nitice);
        bindWidget();
        initialInstance();
        setUp();
        setEventListener();
    }

    private void initialInstance() {

        spdf = new SimpleDateFormat("hh:mm:ss");

        handler = new Handler();
    }

    private void setUp() {

        runnable = new Runnable() {
            @Override
            public void run() {
                Date date = new Date();
                String current = spdf.format(date);
                tv_display_clock.setText(String.valueOf(current));
                handler.postDelayed(runnable, 1000);
            }
        };

        handler.postDelayed(runnable, 1000);

        btn_notification.setOnClickListener(this);

    }

    private void bindWidget() {

        tv_display_clock = findViewById(R.id.tv_display_time);

        btn_notification = findViewById(R.id.btn_notification);

        et_hour = findViewById(R.id.et_hour);
        et_minute = findViewById(R.id.et_minute);
    }

    private void setEventListener(){

        et_hour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mHour = s.toString();
            }
        });

        et_minute.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mMinute = s.toString();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_notification){
            startAlarm(false, Integer.valueOf(mHour), Integer.valueOf(mMinute));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startAlarm(boolean isRepeat, int hour, int minute){
        Toast.makeText(this, "Start!", Toast.LENGTH_LONG).show();
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        int uniqueId = (int) System.currentTimeMillis();

        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = new NotificationChannel(String.valueOf(uniqueId), "testtttt", importance);
        mChannel.setDescription("Welcome to Notification Service");
        mChannel.enableLights(true);
        mChannel.enableVibration(true);
        mChannel.setShowBadge(true);
        mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

        Intent myIntent = new Intent(NiticeActivity.this, AlarmNotificationReceiver.class);
        myIntent.putExtra("notificationChannel", mChannel);
        myIntent.putExtra("id", uniqueId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, uniqueId, myIntent, 0);

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
