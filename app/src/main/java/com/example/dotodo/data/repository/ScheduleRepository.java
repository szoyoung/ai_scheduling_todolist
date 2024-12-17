package com.example.dotodo.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.dotodo.data.database.AppDatabase;
import com.example.dotodo.data.database.ScheduleDao;
import com.example.dotodo.data.model.Schedule;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScheduleRepository {
    private final ScheduleDao scheduleDao;
    private final LiveData<List<Schedule>> allSchedules;
    private final ExecutorService executorService;

    public ScheduleRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        scheduleDao = database.scheduleDao();
        allSchedules = scheduleDao.getAllSchedules();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<Schedule> getCurrentSchedule() {
        return scheduleDao.getCurrentSchedule();
    }

    public LiveData<List<Schedule>> getAllSchedules() {
        return allSchedules;
    }

    public LiveData<Schedule> getScheduleById(int id) {
        return scheduleDao.getScheduleById(id);
    }

    public void insert(Schedule schedule) {
        executorService.execute(() -> scheduleDao.insert(schedule));
    }

    public void update(Schedule schedule) {
        executorService.execute(() -> scheduleDao.update(schedule));
    }

    public void delete(Schedule schedule) {
        executorService.execute(() -> scheduleDao.delete(schedule));
    }
}