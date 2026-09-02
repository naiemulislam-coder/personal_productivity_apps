package com.naiemulislam.productivity.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.naiemulislam.productivity.data.entity.FixedActivity;

import java.util.List;

@Dao
public interface FixedActivityDao {
    @Insert
    long insert(FixedActivity fixedActivity);

    @Query("SELECT * FROM fixed_activities ORDER BY startTime ASC")
    List<FixedActivity> getAll();
}
