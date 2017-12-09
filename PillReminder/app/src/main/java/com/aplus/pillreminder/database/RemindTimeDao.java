package com.aplus.pillreminder.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.aplus.pillreminder.model.RemindTime;

import java.util.List;

@Dao
public interface RemindTimeDao {
    @Update
    void update(RemindTime remindTime);

    @Insert
    void insert(RemindTime remindTime);
}
