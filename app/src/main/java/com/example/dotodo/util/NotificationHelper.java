package com.example.dotodo.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;

import com.example.dotodo.MainActivity;
import com.example.dotodo.R;
import com.example.dotodo.data.model.Task;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class NotificationHelper {
    private Context context;
    private NotificationManager notificationManager;
    private static final String CHANNEL_ID = "task_reminder";
    private static final String CHANNEL_NAME = "Task Reminders";

    public NotificationHelper(Context context) {
        this.context = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setShowBadge(true);
            channel.enableVibration(true);
            channel.enableLights(true);
            channel.setDescription("할 일 미완료 알림");

            notificationManager.createNotificationChannel(channel);
        }
    }

    public void showTaskReminder(Task task, int notificationTime) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                task.getId(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd", Locale.getDefault());
        String deadlineStr = dateFormat.format(task.getDeadline());

        // 우선순위에 따른 알림 제목 설정
        String priorityText;
        switch (task.getPriority()) {
            case 2:
                priorityText = "[높은 우선순위]";
                break;
            case 1:
                priorityText = "[보통 우선순위]";
                break;
            default:
                priorityText = "[낮은 우선순위]";
                break;
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(priorityText + " " + task.getTitle())
                .setContentText("내일(" + deadlineStr + ")이 마감일입니다.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        // 각 시간대별로 고유한 알림 ID 생성
        int notificationId = task.getId() + (notificationTime * 100);
        notificationManager.notify(notificationId, builder.build());
    }
}