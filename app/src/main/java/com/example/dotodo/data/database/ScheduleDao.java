package com.example.dotodo.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.dotodo.data.model.Schedule;

import java.util.List;

@Dao
public interface ScheduleDao {
    @Query("SELECT * FROM schedules ORDER BY createDate DESC")
    LiveData<List<Schedule>> getAllSchedules();

    @Insert
    void insert(Schedule schedule);

    @Update
    void update(Schedule schedule);

    @Delete
    void delete(Schedule schedule);

    @Query("SELECT * FROM schedules WHERE id = :id")
    LiveData<Schedule> getScheduleById(int id);

    @Query("SELECT * FROM schedules ORDER BY createDate DESC LIMIT 1")
    LiveData<Schedule> getCurrentSchedule();
}