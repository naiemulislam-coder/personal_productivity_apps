package com.naiemulislam.productivity.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.naiemulislam.productivity.data.entity.Routine;

import java.util.List;

@Dao
public interface RoutineDao {
    @Insert
    long insert(Routine routine);

    @Query("SELECT * FROM routines")
    List<Routine> getAll();
}
