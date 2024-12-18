package com.example.dotodo.receiver;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.example.dotodo.util.NotificationHelper;
import com.example.dotodo.data.model.Task;
import com.example.dotodo.data.repository.TaskRepository;
import java.util.Calendar;
import java.util.Date;

public class AlarmReceiver extends BroadcastReceiver {
    public static final String NOTIFICATION_TIME_EXTRA = "notification_time";

    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationTime = intent.getIntExtra(NOTIFICATION_TIME_EXTRA, 9); // 기본값은 아침 9시

        TaskRepository repository = new TaskRepository((Application) context.getApplicationContext());
        NotificationHelper notificationHelper = new NotificationHelper(context);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1); // 내일 날짜
        Date tomorrow = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR, -1); // 오늘 날짜로 되돌림
        Date today = calendar.getTime();

        repository.getUncompletedTasksDueTomorrow(today, tomorrow).observeForever(tasks -> {
            if (tasks != null) {
                for (Task task : tasks) {
                    // 우선순위에 따라 알림 시간 필터링
                    switch (task.getPriority()) {
                        case 2: // 높은 우선순위: 아침(9시), 점심(13시), 저녁(19시)
                            if (notificationTime == 9 || notificationTime == 13 || notificationTime == 19) {
                                notificationHelper.showTaskReminder(task, notificationTime);
                            }
                            break;
                        case 1: // 보통 우선순위: 아침(9시), 저녁(19시)
                            if (notificationTime == 9 || notificationTime == 19) {
                                notificationHelper.showTaskReminder(task, notificationTime);
                            }
                            break;
                        default: // 낮은 우선순위: 아침(9시)만
                            if (notificationTime == 9) {
                                notificationHelper.showTaskReminder(task, notificationTime);
                            }
                            break;
                    }
                }
            }
        });
    }

    public static void setAlarms(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // 아침 9시 알람
        setDailyAlarm(context, alarmManager, 9, 0);

        // 점심 13시 알람
        setDailyAlarm(context, alarmManager, 13, 0);

        // 저녁 19시 알람
        setDailyAlarm(context, alarmManager, 19, 0);
    }

    private static void setDailyAlarm(Context context, AlarmManager alarmManager, int hour, int minute) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(NOTIFICATION_TIME_EXTRA, hour);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                hour, // 각 시간대별로 고유한 요청 코드 사용
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        // 이미 지난 시간이면 다음날로 설정
        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                pendingIntent
        );
    }
}