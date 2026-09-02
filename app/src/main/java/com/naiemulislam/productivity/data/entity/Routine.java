package com.naiemulislam.productivity.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "routines")
public class Routine {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public String name; // e.g., "Default", "Weekday"
    public boolean isVariable; // variable routine allows day-wise overrides
    public int dayOfWeek; // 0=Sunday..6=Saturday for day-specific routines, -1 for generic
    public long wakeTime; // epoch millis or time-of-day encoded (e.g., minutes since midnight)
    public long sleepTime; // same encoding

    public Routine() {}
}
