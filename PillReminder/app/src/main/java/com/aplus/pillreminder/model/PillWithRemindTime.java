package com.aplus.pillreminder.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

public class PillWithRemindTime {

    @Embedded
    private Pill pill;

    @Relation(parentColumn = "id", entityColumn = "pillId", entity = RemindTime.class)
    private List<RemindTime> remindTimeList;

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
}
