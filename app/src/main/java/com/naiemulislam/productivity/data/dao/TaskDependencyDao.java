package com.naiemulislam.productivity.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.naiemulislam.productivity.data.entity.TaskDependency;

import java.util.List;

@Dao
public interface TaskDependencyDao {
    @Insert
    long insert(TaskDependency d);

    @Query("SELECT * FROM task_dependencies WHERE task_id = :taskId ORDER BY id ASC")
    List<TaskDependency> getDependenciesForTask(long taskId);

    @Query("DELETE FROM task_dependencies WHERE task_id = :taskId")
    void deleteForTask(long taskId);
}
