package com.naiemulislam.productivity.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.naiemulislam.productivity.data.entity.DailyScore;

import java.util.List;

@Dao
public interface DailyScoreDao {
    @Insert
    long insert(DailyScore score);

    @Query("SELECT * FROM daily_scores ORDER BY date DESC")
    List<DailyScore> getAll();
}
