package com.naiemulislam.productivity.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Explicit many-to-many dependency table: task A depends on task B.
 */
@Entity(tableName = "task_dependencies")
public class TaskDependency {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "task_id")
    public long taskId;

    @ColumnInfo(name = "depends_on_id")
    public long dependsOnTaskId;

    @ColumnInfo(name = "type")
    public String type; // optional: "finish_to_start" etc.

    public TaskDependency() {}
}
