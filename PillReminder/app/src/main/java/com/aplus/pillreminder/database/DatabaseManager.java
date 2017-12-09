package com.aplus.pillreminder.database;

import android.arch.persistence.room.Room;
import android.content.Context;

public class DatabaseManager {
    private static DatabaseManager instance;
    private PillReminderDb db;

    private DatabaseManager() {
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public void initDb(Context context) {
        db = Room.databaseBuilder(context,
                PillReminderDb.class, "PillReminder.db")
                .fallbackToDestructiveMigration()
                .build();
    }

    public PillReminderDb getDb() {
        return db;
    }
}
