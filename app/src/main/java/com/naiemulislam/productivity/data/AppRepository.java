package com.naiemulislam.productivity.data;

import android.content.Context;

import com.naiemulislam.productivity.data.db.AppDatabase;
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

import java.util.List;

public class AppRepository {
    private final TaskDao taskDao;
    private final GoalDao goalDao;
    private final RoutineDao routineDao;
    private final FixedActivityDao fixedActivityDao;
    private final FocusSessionDao focusSessionDao;
    private final DailyScoreDao dailyScoreDao;
    private final RescheduleHistoryDao rescheduleHistoryDao;

    public AppRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        taskDao = db.taskDao();
        goalDao = db.goalDao();
        routineDao = db.routineDao();
        fixedActivityDao = db.fixedActivityDao();
        focusSessionDao = db.focusSessionDao();
        dailyScoreDao = db.dailyScoreDao();
        rescheduleHistoryDao = db.rescheduleHistoryDao();
    }

    // Task
    public long insertTask(Task task) { return taskDao.insert(task); }
    public List<Task> getAllTasks() { return taskDao.getAll(); }

    // Goal
    public long insertGoal(Goal goal) { return goalDao.insert(goal); }
    public List<Goal> getAllGoals() { return goalDao.getAll(); }

    // Routine
    public long insertRoutine(Routine routine) { return routineDao.insert(routine); }
    public List<Routine> getAllRoutines() { return routineDao.getAll(); }

    // Fixed Activity
    public long insertFixedActivity(FixedActivity fa) { return fixedActivityDao.insert(fa); }
    public List<FixedActivity> getAllFixedActivities() { return fixedActivityDao.getAll(); }

    // Focus Session
    public long insertFocusSession(FocusSession s) { return focusSessionDao.insert(s); }
    public List<FocusSession> getAllFocusSessions() { return focusSessionDao.getAll(); }

    // Daily Score
    public long insertDailyScore(DailyScore s) { return dailyScoreDao.insert(s); }
    public List<DailyScore> getAllDailyScores() { return dailyScoreDao.getAll(); }

    // Reschedule History
    public long insertRescheduleHistory(RescheduleHistory h) { return rescheduleHistoryDao.insert(h); }
    public List<RescheduleHistory> getAllRescheduleHistory() { return rescheduleHistoryDao.getAll(); }
}
