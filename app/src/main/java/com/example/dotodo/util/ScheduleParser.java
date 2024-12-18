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

        prompt.append("당신은 개인 일정 관리 전문가입니다. 다음 조건들을 모두 만족하는 최적의 주간 스케줄을 생성해주세요.\n\n");

        // 핵심 제약조건
        prompt.append("【필수 준수사항】\n");
        prompt.append("1. 모든 Task는 반드시 마감일 전에 완료되어야 합니다.\n");
        prompt.append("2. 우선순위가 높은 Task를 먼저 배치해야 합니다.\n");
        prompt.append("3. 고정 스케줄은 절대 변경할 수 없으며, 반드시 포함되어야 합니다.\n");
        prompt.append("4. Task의 세부사항에 명시된 조건들을 반드시 고려해야 합니다.\n\n");

        // 우선순위가 높은 작업 먼저 표시
        prompt.append("【높은 우선순위 작업】\n");
        for (Task task : tasks) {
            if (!task.isCompleted() && task.getPriority() == 2) {
                appendTaskDetails(prompt, task);
            }
        }

        // 중간 우선순위 작업
        prompt.append("\n【보통 우선순위 작업】\n");
        for (Task task : tasks) {
            if (!task.isCompleted() && task.getPriority() == 1) {
                appendTaskDetails(prompt, task);
            }
        }

        // 낮은 우선순위 작업
        prompt.append("\n【낮은 우선순위 작업】\n");
        for (Task task : tasks) {
            if (!task.isCompleted() && task.getPriority() == 0) {
                appendTaskDetails(prompt, task);
            }
        }

        // 고정 스케줄을 표시
        prompt.append("\n【변경 불가능한 고정 스케줄】\n");
        prompt.append(fixedSchedule).append("\n\n");

        // 스케줄 생성 기간 명시
        prompt.append("【스케줄 생성 기간】\n");
        prompt.append(dateFormat.format(startDate)).append("부터 7일간\n\n");

        // 응답 형식
        prompt.append("【응답 형식】\n");
        prompt.append("각 날짜는 === 구분선으로 구분하고, 다음 형식을 정확히 따라주세요:\n\n");
        prompt.append("===\n");
        prompt.append("YYYY-MM-DD\n");
        prompt.append("HH:mm-HH:mm: (만약 우선순위가 높다면 앞에 ⭐️을 붙여주세요) 일정내용 (Task 또는 고정일정)\n");
        prompt.append("===\n\n");

        // 품질 체크 리스트
        prompt.append("【스케줄 생성 전 확인사항】\n");
        prompt.append("1. 모든 고정 스케줄이 정확히 포함되었는가?\n");
        prompt.append("2. 우선순위가 높은 작업이 먼저 배치되었는가?\n");
        prompt.append("3. 모든 작업이 마감일 전에 완료되도록 계획되었는가?\n");
        prompt.append("4. 작업 간 이동 시간이 충분히 고려되었는가?\n");
        prompt.append("5. 각 작업의 세부사항이 모두 반영되었는가?\n");

        return prompt.toString();
    }

    private static void appendTaskDetails(StringBuilder prompt, Task task) {
        prompt.append("• ").append(task.getTitle()).append("\n");
        prompt.append("  - 마감일: ").append(DATE_FORMAT.format(task.getDeadline())).append("\n");
        if (task.getDescription() != null && !task.getDescription().isEmpty()) {
            prompt.append("  - 세부사항: ").append(task.getDescription()).append("\n");
        }
        prompt.append("  - 우선순위: ").append(getPriorityString(task.getPriority())).append("\n");
    }

    private static String getPriorityString(int priority) {
        switch (priority) {
            case 2: return "높음";
            case 1: return "보통";
            default: return "낮음";
        }
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