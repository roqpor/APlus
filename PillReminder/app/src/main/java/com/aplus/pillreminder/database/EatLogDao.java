package com.aplus.pillreminder.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Update;

import com.aplus.pillreminder.model.EatLog;

import java.util.Date;
import java.util.List;

@Dao
@TypeConverters({DateConverter.class})
public interface EatLogDao {

    @Query("SELECT * FROM EatLog")
    List<EatLog> getAll();

    @Query("SELECT * FROM EatLog WHERE isTaken = 0 AND date BETWEEN :dayStart AND :dayEnd")
    List<EatLog> getNotTakenLogs(Date dayStart, Date dayEnd);

    @Query("SELECT * FROM EatLog WHERE pillId = :pillId AND isTaken = 0 AND date BETWEEN :dayStart AND :dayEnd")
    List<EatLog> getNotTakenLogs(int pillId, Date dayStart, Date dayEnd);

    @Query("SELECT * FROM EatLog WHERE pillId = :pillId AND isTaken = 0 AND date = :date")
    EatLog getNotTakenLog(int pillId, Date date);

    @Insert
    void insert(List<EatLog> eatLogs);

    @Delete
    void delete(List<EatLog> eatLogs);

    @Update
    void update(EatLog eatLog);
}
