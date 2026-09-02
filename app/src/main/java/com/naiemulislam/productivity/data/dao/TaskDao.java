package com.naiemulislam.productivity.data.dao;

import androidx.room.Dao;
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
    void update(Task task);

    @Query("SELECT * FROM tasks ORDER BY priority DESC, deadline ASC")
    List<Task> getAll();

    @Query("SELECT * FROM tasks WHERE id = :id LIMIT 1")
    Task findById(long id);
}
