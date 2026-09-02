package com.naiemulislam.productivity.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "focus_sessions")
public class FocusSession {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public long taskId; // FK to Task.id
    public long plannedDurationMinutes;
    public long actualDurationMinutes;
    public long startTime; // epoch millis
    public long endTime; // epoch millis

    public FocusSession() {}
}
