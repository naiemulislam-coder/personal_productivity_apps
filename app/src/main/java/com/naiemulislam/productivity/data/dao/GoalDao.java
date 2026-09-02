package com.naiemulislam.productivity.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.naiemulislam.productivity.data.entity.Goal;

import java.util.List;

@Dao
public interface GoalDao {
    @Insert
    long insert(Goal goal);

    @Query("SELECT * FROM goals ORDER BY targetDate ASC")
    List<Goal> getAll();
}
