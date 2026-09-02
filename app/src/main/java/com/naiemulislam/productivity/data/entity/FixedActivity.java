package com.naiemulislam.productivity.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "fixed_activities")
public class FixedActivity {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public String name; // e.g., "Work", "Prayer"
    public long startTime; // epoch millis or time-of-day encoding depending on design
    public int durationMinutes;
    public String repeatPattern; // simple cron-like or enum (e.g., DAILY, WEEKDAYS)

    public FixedActivity() {}
}
