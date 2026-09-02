package com.naiemulislam.productivity.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Task entity representing a user's task.
 * This schema is designed to support scheduling, dependencies and tracking.
 */
@Entity(tableName = "tasks")
public class Task {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "notes")
    public String notes;

    @ColumnInfo(name = "estimated_minutes")
    public int estimatedMinutes; // planned estimate in minutes

    @ColumnInfo(name = "priority")
    public int priority; // 0=low,1=med,2=high

    @ColumnInfo(name = "deadline")
    public long deadline; // epoch millis, 0 = none

    @ColumnInfo(name = "allow_chunking")
    public boolean allowChunking;

    @ColumnInfo(name = "difficulty")
    public int difficulty; // 0..10 scale, optional

    @ColumnInfo(name = "is_fixed")
    public boolean isFixed; // fixed tasks map to FixedActivity like slots

    @ColumnInfo(name = "planned_start")
    public Long plannedStart; // nullable epoch millis

    @ColumnInfo(name = "planned_end")
    public Long plannedEnd; // nullable epoch millis

    @ColumnInfo(name = "status")
    public String status; // e.g., TODO, IN_PROGRESS, DONE

    @ColumnInfo(name = "parent_task_id")
    public Long parentTaskId; // optional parent task (for simple dependency)

    @ColumnInfo(name = "created_at")
    public long createdAt;

    @ColumnInfo(name = "updated_at")
    public long updatedAt;

    public Task() {}
}
