package com.example.dotodo.util;

import com.example.dotodo.data.model.DaySchedule;
import com.example.dotodo.data.model.ScheduleEvent;
import com.example.dotodo.data.model.Task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScheduleParser {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private static final Pattern TIME_PATTERN = Pattern.compile("(\\d{2}:\\d{2})\\s*-\\s*(\\d{2}:\\d{2})\\s*:\\s*(.+)");

    public static String buildPrompt(List<Task> tasks, String fixedSchedule, Date startDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        StringBuilder prompt = new StringBuilder();
        prompt.append("다음 정보를 바탕으로 ").append(dateFormat.format(startDate))
                .append("부터 7일간의 일정을 만들어줘.\n\n");

        // 할 일 목록 추가 - 세부사항 포함
        prompt.append("【할 일 목록】\n");
        for (Task task : tasks) {
            if (task.isCompleted()) {
                continue; // 완료된 할일은 제외
            }
            prompt.append(String.format("- %s\n", task.getTitle()));
            // 세부사항이 있는 경우 추가
            if (task.getDescription() != null && !task.getDescription().isEmpty()) {
                prompt.append("  세부 사항: ").append(task.getDescription()).append("\n");
            }
            // 우선순위 정보 추가
            String priority;
            switch (task.getPriority()) {
                case 2:
                    priority = "높음";
                    break;
                case 1:
                    priority = "보통";
                    break;
                default:
                    priority = "낮음";
                    break;
            }
            prompt.append("  우선순위: ").append(priority).append("\n");
            // 마감일 추가
            prompt.append("  마감일: ").append(DATE_FORMAT.format(task.getDeadline())).append("\n\n");
        }

        // 고정 스케줄 추가
        prompt.append("\n【고정 스케줄】\n").append(fixedSchedule).append("\n\n");

        // 응답 형식 지정
        prompt.append("\n\n다음 형식으로 각 날짜별 일정을 모두 작성해줘:\n");
        prompt.append("===\n");
        prompt.append("2024-12-08\n");  // 예시 날짜
        prompt.append("09:00-10:30: 일정1\n");
        prompt.append("11:00-12:00: 일정2\n");
        prompt.append("===\n");
        prompt.append("2024-12-09\n");  // 다음 날 예시 추가
        prompt.append("09:00-10:30: 일정1\n");
        prompt.append("===\n");
        prompt.append("이런 식으로 **7일 모두** 작성해. 각 날짜는 === 구분선으로 구분해.\n");
        prompt.append("그리고 하루의 모든 시간에 맞춰 일정을 작성할 필요는 없어.\n");
        prompt.append("스케줄을 작성할때에는 **우선순위와 마감일을 꼭!!** 고려하고, 할 일 목록에 있는 task의 내용 그대로 작성해.\n");
        prompt.append("【중요】\n");
        prompt.append("- 스케줄링할 때 마감일을 절대 넘기면 안돼!!!.\n");
        prompt.append("- task의 세부 사항도 참고해서 스케줄링 해야해.\n");
        prompt.append("- 고정 스케줄이 있다면 그것도 고려해서 스케줄링 해야해.\n");
        prompt.append("- 입력된 **고정 스케줄도 꼭!! 포함해서** 스케줄을 작성해줘.\n");
        return prompt.toString();
    }

    public static List<DaySchedule> parseResponse(String response) {
        List<DaySchedule> weekSchedule = new ArrayList<>();
        String[] days = response.split("===");

        for (String day : days) {
            if (day.trim().isEmpty()) continue;

            String[] lines = day.trim().split("\n");
            if (lines.length < 1) continue;

            try {
                // 첫 줄은 날짜
                Date date = DATE_FORMAT.parse(lines[0].trim());
                DaySchedule daySchedule = new DaySchedule(date);

                // 나머지 줄들은 일정
                for (int i = 1; i < lines.length; i++) {
                    Matcher matcher = TIME_PATTERN.matcher(lines[i].trim());
                    if (matcher.find()) {
                        String startTime = matcher.group(1);
                        String endTime = matcher.group(2);
                        String title = matcher.group(3);

                        ScheduleEvent event = new ScheduleEvent(
                                startTime,
                                endTime,
                                title,
                                title.contains("고정") ? ScheduleEvent.EventType.FIXED : ScheduleEvent.EventType.TASK
                        );

                        daySchedule.addEvent(event);
                    }
                }

                daySchedule.sortEventsByTime();
                weekSchedule.add(daySchedule);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return weekSchedule;
    }
}