package com.naiemulislam.productivity.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "reschedule_history")
public class RescheduleHistory {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public long taskId;
    public long previousPlannedTime; // epoch millis
    public long newPlannedTime; // epoch millis
    public String reason; // optional reason text
    public long timestamp; // when rescheduled

    public RescheduleHistory() {}
}
