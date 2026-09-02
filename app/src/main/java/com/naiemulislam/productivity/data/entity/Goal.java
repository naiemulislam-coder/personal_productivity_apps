package com.naiemulislam.productivity.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "goals")
public class Goal {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public String name;
    public String description;
    public long targetDate; // epoch millis, optional
    public int progressPercent; // cached progress based on related tasks (0-100)

    public Goal() {}
}
