package com.example.dotodo.data.model;

public class ScheduleEvent {
    public enum EventType {
        TASK,       // Task에서 생성된 일정
        FIXED       // 고정 스케줄
    }

    private String startTime;  // "HH:mm" 형식 (예: "09:30")
    private String endTime;    // "HH:mm" 형식
    private String title;
    private EventType type;

    public ScheduleEvent(String startTime, String endTime, String title, EventType type) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.title = title;
        this.type = type;
    }

    // Getters and Setters
    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

}