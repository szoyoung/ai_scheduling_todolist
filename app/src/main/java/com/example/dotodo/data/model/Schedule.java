package com.example.dotodo.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import com.example.dotodo.data.database.DateConverter;

import java.util.Date;
import java.util.List;

@Entity(tableName = "schedules")
public class Schedule {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private Date createDate;  // 스케줄 생성 날짜
    private String title;     // 스케줄 제목 (예: "2024년 12월 첫째주 스케줄")

    @TypeConverters(DateConverter.class)
    private List<DaySchedule> weeklySchedule; // 일주일 스케줄 데이터

    // Constructor
    public Schedule(Date createDate, String title, List<DaySchedule> weeklySchedule) {
        this.createDate = createDate;
        this.title = title;
        this.weeklySchedule = weeklySchedule;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Date getCreateDate() { return createDate; }
    public void setCreateDate(Date createDate) { this.createDate = createDate; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public List<DaySchedule> getWeeklySchedule() { return weeklySchedule; }
    public void setWeeklySchedule(List<DaySchedule> weeklySchedule) { this.weeklySchedule = weeklySchedule; }
}