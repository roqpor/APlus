package com.aplus.pillreminder.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;

import com.aplus.pillreminder.model.RemindTime;

@Dao
public interface RemindTimeDao {
    @Insert
    long insert(RemindTime remindTime);

    @Delete
    void delete(RemindTime remindTime);
}
