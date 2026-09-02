package com.naiemulislam.productivity.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.naiemulislam.productivity.data.entity.RescheduleHistory;

import java.util.List;

@Dao
public interface RescheduleHistoryDao {
    @Insert
    long insert(RescheduleHistory history);

    @Query("SELECT * FROM reschedule_history ORDER BY timestamp DESC")
    List<RescheduleHistory> getAll();
}
