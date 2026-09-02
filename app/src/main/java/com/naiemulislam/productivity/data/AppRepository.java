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
import com.naiemulislam.productivity.data.dao.TaskDependencyDao;
import com.naiemulislam.productivity.data.entity.Task;
import com.naiemulislam.productivity.data.entity.Goal;
import com.naiemulislam.productivity.data.entity.Routine;
import com.naiemulislam.productivity.data.entity.FixedActivity;
import com.naiemulislam.productivity.data.entity.FocusSession;
import com.naiemulislam.productivity.data.entity.DailyScore;
import com.naiemulislam.productivity.data.entity.RescheduleHistory;
import com.naiemulislam.productivity.data.entity.TaskDependency;

import java.util.List;

public class AppRepository {
    private final TaskDao taskDao;
    private final GoalDao goalDao;
    private final RoutineDao routineDao;
    private final FixedActivityDao fixedActivityDao;
    private final FocusSessionDao focusSessionDao;
    private final DailyScoreDao dailyScoreDao;
    private final RescheduleHistoryDao rescheduleHistoryDao;
    private final TaskDependencyDao taskDependencyDao;

    public AppRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        taskDao = db.taskDao();
        goalDao = db.goalDao();
        routineDao = db.routineDao();
        fixedActivityDao = db.fixedActivityDao();
        focusSessionDao = db.focusSessionDao();
        dailyScoreDao = db.dailyScoreDao();
        rescheduleHistoryDao = db.rescheduleHistoryDao();
        taskDependencyDao = db.taskDependencyDao();
    }

    // Task
    public long insertTask(Task task) { return taskDao.insert(task); }
    public int updateTask(Task task) { return taskDao.update(task); }
    public int deleteTask(Task task) { return taskDao.delete(task); }
    public List<Task> getAllTasks() { return taskDao.getAll(); }
    public Task getTaskById(long id) { return taskDao.getById(id); }
    public List<Task> getTasksForWindow(long start, long end) { return taskDao.getTasksForWindow(start, end); }
    public List<Task> getUnscheduledTasks() { return taskDao.getUnscheduled(); }

    // Task dependencies
    public long insertDependency(TaskDependency d) { return taskDependencyDao.insert(d); }
    public List<TaskDependency> getDependenciesForTask(long taskId) { return taskDependencyDao.getDependenciesForTask(taskId); }
    public void deleteDependenciesForTask(long taskId) { taskDependencyDao.deleteForTask(taskId); }

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
