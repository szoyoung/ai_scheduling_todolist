package com.example.dotodo.data.database;

import androidx.room.TypeConverter;
import com.example.dotodo.data.model.DaySchedule;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ScheduleConverter {
    static Gson gson = new Gson();

    @TypeConverter
    public static String fromScheduleList(List<DaySchedule> schedule) {
        if (schedule == null) {
            return null;
        }
        return gson.toJson(schedule);
    }

    @TypeConverter
    public static List<DaySchedule> toScheduleList(String scheduleString) {
        if (scheduleString == null) {
            return null;
        }
        Type listType = new TypeToken<List<DaySchedule>>() {}.getType();
        return gson.fromJson(scheduleString, listType);
    }
}