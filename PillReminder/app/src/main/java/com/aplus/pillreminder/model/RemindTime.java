package com.aplus.pillreminder.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(foreignKeys = {
        @ForeignKey(entity = Pill.class,
                    parentColumns = "id",
                    childColumns = "pillId",
                    onDelete = ForeignKey.CASCADE)
})
public class RemindTime implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int pillId;

    private int hour;

    private int minute;

    public RemindTime() {
    }

    protected RemindTime(Parcel in) {
        id = in.readInt();
        pillId = in.readInt();
        hour = in.readInt();
        minute = in.readInt();
    }

    public static final Creator<RemindTime> CREATOR = new Creator<RemindTime>() {
        @Override
        public RemindTime createFromParcel(Parcel in) {
            return new RemindTime(in);
        }

        @Override
        public RemindTime[] newArray(int size) {
            return new RemindTime[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPillId() {
        return pillId;
    }

    public void setPillId(int pillId) {
        this.pillId = pillId;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getTotalMinute() {
        return hour * 60 + minute;
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d", hour, minute);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(pillId);
        parcel.writeInt(hour);
        parcel.writeInt(minute);
    }
}
