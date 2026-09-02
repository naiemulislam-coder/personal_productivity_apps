package com.naiemulislam.productivity.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.naiemulislam.productivity.data.entity.FocusSession;

import java.util.List;

@Dao
public interface FocusSessionDao {
    @Insert
    long insert(FocusSession session);

    @Query("SELECT * FROM focus_sessions ORDER BY startTime DESC")
    List<FocusSession> getAll();
}
