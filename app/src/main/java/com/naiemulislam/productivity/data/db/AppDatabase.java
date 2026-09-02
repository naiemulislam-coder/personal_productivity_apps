package com.naiemulislam.productivity.data.db;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.naiemulislam.productivity.data.dao.TaskDao;
import com.naiemulislam.productivity.data.dao.GoalDao;
import com.naiemulislam.productivity.data.dao.RoutineDao;
import com.naiemulislam.productivity.data.dao.FixedActivityDao;
import com.naiemulislam.productivity.data.dao.FocusSessionDao;
import com.naiemulislam.productivity.data.dao.DailyScoreDao;
import com.naiemulislam.productivity.data.dao.RescheduleHistoryDao;

import com.naiemulislam.productivity.data.entity.Task;
import com.naiemulislam.productivity.data.entity.Goal;
import com.naiemulislam.productivity.data.entity.Routine;
import com.naiemulislam.productivity.data.entity.FixedActivity;
import com.naiemulislam.productivity.data.entity.FocusSession;
import com.naiemulislam.productivity.data.entity.DailyScore;
import com.naiemulislam.productivity.data.entity.RescheduleHistory;

@Database(entities = {Task.class, Goal.class, Routine.class, FixedActivity.class, FocusSession.class, DailyScore.class, RescheduleHistory.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();
    public abstract GoalDao goalDao();
    public abstract RoutineDao routineDao();
    public abstract FixedActivityDao fixedActivityDao();
    public abstract FocusSessionDao focusSessionDao();
    public abstract DailyScoreDao dailyScoreDao();
    public abstract RescheduleHistoryDao rescheduleHistoryDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "productivity_db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
