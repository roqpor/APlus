package com.aplus.pillreminder.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.aplus.pillreminder.database.DateConverter;

import java.util.Date;

@Entity
public class EatLog {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @TypeConverters({DateConverter.class})
    private Date date;

    private boolean isTaken;

    private int pillId;

    private String pillName;

    private int dose;

    public String getTime() {
        return String.format("%02d:%02d", date.getHours(), date.getMinutes());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isTaken() {
        return isTaken;
    }

    public void setTaken(boolean taken) {
        isTaken = taken;
    }

    public int getPillId() {
        return pillId;
    }

    public void setPillId(int pillId) {
        this.pillId = pillId;
    }

    public String getPillName() {
        return pillName;
    }

    public void setPillName(String pillName) {
        this.pillName = pillName;
    }

    public int getDose() {
        return dose;
    }

    public void setDose(int dose) {
        this.dose = dose;
    }
}
