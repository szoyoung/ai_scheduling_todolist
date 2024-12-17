package com.example.dotodo.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.dotodo.data.database.AppDatabase;
import com.example.dotodo.data.database.TaskDao;
import com.example.dotodo.data.model.Task;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskRepository {
    private TaskDao taskDao;
    private LiveData<List<Task>> allTasks;
    private ExecutorService executorService;

    public TaskRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        taskDao = database.taskDao();
        allTasks = taskDao.getAllTasks();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public void insert(Task task) {
        executorService.execute(() -> taskDao.insert(task));
    }

    public void update(Task task) {
        executorService.execute(() -> taskDao.update(task));
    }

    public void delete(Task task) {
        executorService.execute(() -> taskDao.delete(task));
    }

    public LiveData<List<Task>> getUncompletedTasksDueTomorrow(Date today, Date tomorrow) {
        return taskDao.getUncompletedTasksDueTomorrow(today.getTime(), tomorrow.getTime());
    }
}