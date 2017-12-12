package com.aplus.pillreminder.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.aplus.pillreminder.model.EatLog;
import com.aplus.pillreminder.model.Pill;
import com.aplus.pillreminder.model.RemindTime;

@Database(entities = {Pill.class, RemindTime.class, EatLog.class}, version = 5)
public abstract class PillReminderDb extends RoomDatabase {
    public abstract PillDao pillDao();
    public abstract RemindTimeDao remindTimeDao();
    public abstract EatLogDao eatLogDao();
}
