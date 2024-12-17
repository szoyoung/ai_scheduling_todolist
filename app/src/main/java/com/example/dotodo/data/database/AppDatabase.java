package com.example.dotodo.data.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.dotodo.data.model.Schedule;
import com.example.dotodo.data.model.Task;

@Database(entities = {Task.class, Schedule.class}, version = 2)
@TypeConverters({DateConverter.class, ScheduleConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract TaskDao taskDao();
    public abstract ScheduleDao scheduleDao();
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "todo_database"
            )
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}