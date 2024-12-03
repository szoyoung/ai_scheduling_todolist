package com.example.dotodo.receiver;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.example.dotodo.NotificationHelper;
import com.example.dotodo.data.model.Task;
import com.example.dotodo.data.repository.TaskRepository;

import java.util.Calendar;
import java.util.Date;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        TaskRepository repository = new TaskRepository((Application) context.getApplicationContext());
        NotificationHelper notificationHelper = new NotificationHelper(context);

        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();

        repository.getUncompletedTasksDueTomorrow(today, tomorrow).observeForever(tasks -> {
            // 각 task마다 500ms(0.5초)의 딜레이를 주어 순차적으로 알림 표시
            for (int i = 0; i < tasks.size(); i++) {
                final Task task = tasks.get(i);
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    notificationHelper.showTaskReminder(task);
                }, i * 500L);  // 0.5초씩 딜레이
            }
        });
    }
}