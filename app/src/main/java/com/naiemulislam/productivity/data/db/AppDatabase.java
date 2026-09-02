package com.naiemulislam.productivity.data.db;

import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

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
import com.naiemulislam.productivity.data.dao.TaskDependencyDao;

import com.naiemulislam.productivity.data.entity.Task;
import com.naiemulislam.productivity.data.entity.Goal;
import com.naiemulislam.productivity.data.entity.Routine;
import com.naiemulislam.productivity.data.entity.FixedActivity;
import com.naiemulislam.productivity.data.entity.FocusSession;
import com.naiemulislam.productivity.data.entity.DailyScore;
import com.naiemulislam.productivity.data.entity.RescheduleHistory;
import com.naiemulislam.productivity.data.entity.TaskDependency;

@Database(entities = {Task.class, Goal.class, Routine.class, FixedActivity.class, FocusSession.class, DailyScore.class, RescheduleHistory.class, TaskDependency.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();
    public abstract GoalDao goalDao();
    public abstract RoutineDao routineDao();
    public abstract FixedActivityDao fixedActivityDao();
    public abstract FocusSessionDao focusSessionDao();
    public abstract DailyScoreDao dailyScoreDao();
    public abstract RescheduleHistoryDao rescheduleHistoryDao();
    public abstract TaskDependencyDao taskDependencyDao();

    private static volatile AppDatabase INSTANCE;

    // Migration 2 -> 3: add new columns to tasks and create task_dependencies table
    public static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Add new columns to existing tasks table with defaults
            try {
                database.execSQL("ALTER TABLE tasks ADD COLUMN difficulty INTEGER NOT NULL DEFAULT 0");
            } catch (Exception ignored) {}
            try {
                database.execSQL("ALTER TABLE tasks ADD COLUMN is_fixed INTEGER NOT NULL DEFAULT 0");
            } catch (Exception ignored) {}
            try {
                database.execSQL("ALTER TABLE tasks ADD COLUMN allow_chunking INTEGER NOT NULL DEFAULT 1");
            } catch (Exception ignored) {}
            try {
                database.execSQL("ALTER TABLE tasks ADD COLUMN planned_start INTEGER");
            } catch (Exception ignored) {}
            try {
                database.execSQL("ALTER TABLE tasks ADD COLUMN planned_end INTEGER");
            } catch (Exception ignored) {}
            try {
                database.execSQL("ALTER TABLE tasks ADD COLUMN parent_task_id INTEGER");
            } catch (Exception ignored) {}

            // Create task_dependencies table
            database.execSQL("CREATE TABLE IF NOT EXISTS `task_dependencies` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `task_id` INTEGER NOT NULL, `depends_on_id` INTEGER NOT NULL, `type` TEXT)");
        }
    };

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "productivity_db")
                            .addMigrations(MIGRATION_2_3)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
