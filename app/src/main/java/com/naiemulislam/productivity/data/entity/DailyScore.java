package com.naiemulislam.productivity.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "daily_scores")
public class DailyScore {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public long date; // epoch millis representing the day (e.g., start of day)
    public int score; // 0 - 100
    public int plannedMinutes;
    public int actualFocusMinutes;

    public DailyScore() {}
}
