package com.naiemulislam.productivity.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
public class Task {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public String name;
    public String goalId; // optional
    public long deadline; // epoch millis
    public int estimatedMinutes;
    public int priority; // 0=Low,1=Medium,2=High
    public boolean isFixed;

    public Task() {}
}
