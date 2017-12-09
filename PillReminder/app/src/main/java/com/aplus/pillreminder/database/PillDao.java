package com.aplus.pillreminder.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.aplus.pillreminder.model.Pill;
import com.aplus.pillreminder.model.PillWithRemindTime;

import java.util.List;

@Dao
public interface PillDao {

    @Query("SELECT * FROM Pill")
    List<Pill> getAll();

    @Query("SELECT * FROM Pill")
    List<PillWithRemindTime> loadPillsWithRemindTimes();

    @Query("SELECT * FROM Pill WHERE id = :id ")
    PillWithRemindTime loadPillWithRemindTime(int id);

    @Insert
    long insert(Pill pill);

    @Update
    void update(Pill pill);

    @Delete
    void delete(Pill pill);
}
