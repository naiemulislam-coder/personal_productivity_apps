package com.naiemulislam.productivity.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.naiemulislam.productivity.data.entity.Task;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    long insert(Task task);

    @Update
    int update(Task task);

    @Delete
    int delete(Task task);

    @Query("SELECT * FROM tasks ORDER BY priority DESC, deadline ASC, created_at ASC")
    List<Task> getAll();

    @Query("SELECT * FROM tasks WHERE id = :id LIMIT 1")
    Task getById(long id);

    @Query("SELECT * FROM tasks WHERE planned_start BETWEEN :start AND :end ORDER BY planned_start ASC")
    List<Task> getTasksForWindow(long start, long end);

    @Query("SELECT * FROM tasks WHERE planned_start IS NULL ORDER BY priority DESC, created_at ASC")
    List<Task> getUnscheduled();

    @Query("SELECT * FROM tasks WHERE status != 'DONE' ORDER BY priority DESC, deadline ASC")
    List<Task> getActiveTasks();

    @Query("SELECT COUNT(*) FROM tasks")
    int countAll();
}
