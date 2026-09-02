package com.naiemulislam.productivity.data;

import android.content.Context;

import com.naiemulislam.productivity.data.db.AppDatabase;
import com.naiemulislam.productivity.data.dao.TaskDao;
import com.naiemulislam.productivity.data.entity.Task;

import java.util.List;

public class AppRepository {
    private final TaskDao taskDao;

    public AppRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        taskDao = db.taskDao();
    }

    public long insertTask(Task task) {
        return taskDao.insert(task);
    }

    public List<Task> getAllTasks() {
        return taskDao.getAll();
    }
}
