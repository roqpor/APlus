package com.aplus.pillreminder.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class PillWithRemindTime implements Parcelable {

    @Embedded
    private Pill pill;

    @Relation(parentColumn = "id", entityColumn = "pillId", entity = RemindTime.class)
    private List<RemindTime> remindTimeList;

    public PillWithRemindTime() {
    }

    protected PillWithRemindTime(Parcel in) {
        pill = in.readParcelable(Pill.class.getClassLoader());
        remindTimeList = in.createTypedArrayList(RemindTime.CREATOR);
    }

    public static final Creator<PillWithRemindTime> CREATOR = new Creator<PillWithRemindTime>() {
        @Override
        public PillWithRemindTime createFromParcel(Parcel in) {
            return new PillWithRemindTime(in);
        }

        @Override
        public PillWithRemindTime[] newArray(int size) {
            return new PillWithRemindTime[size];
        }
    };

    public Pill getPill() {
        return pill;
    }

    public void setPill(Pill pill) {
        this.pill = pill;
    }

    public List<RemindTime> getRemindTimeList() {
        return remindTimeList;
    }

    public void setRemindTimeList(List<RemindTime> remindTimeList) {
        this.remindTimeList = remindTimeList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(pill, i);
        parcel.writeTypedList(remindTimeList);
    }
}
