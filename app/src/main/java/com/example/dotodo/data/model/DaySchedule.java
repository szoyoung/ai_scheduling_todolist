package com.example.dotodo.data.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DaySchedule {
    private Date date;                         // 날짜
    private List<ScheduleEvent> events;        // 해당 날짜의 일정들

    public DaySchedule(Date date) {
        this.date = date;
        this.events = new ArrayList<>();
    }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public List<ScheduleEvent> getEvents() { return events; }
    public void setEvents(List<ScheduleEvent> events) { this.events = events; }

    // 일정 추가 편의 메서드
    public void addEvent(ScheduleEvent event) {
        if (events == null) {
            events = new ArrayList<>();
        }
        events.add(event);
    }

    // 시간순 정렬 메서드
    public void sortEventsByTime() {
        if (events != null) {
            events.sort((e1, e2) -> e1.getStartTime().compareTo(e2.getStartTime()));
        }
    }
}