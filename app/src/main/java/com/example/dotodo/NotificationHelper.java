package com.example.dotodo;

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
                    NotificationManager.IMPORTANCE_HIGH  // 팝업 알림을 위해 HIGH로 설정
            );
            // 채널 설정
            channel.setShowBadge(true);           // 앱 아이콘에 배지 표시
            channel.enableVibration(true);        // 진동 사용
            channel.enableLights(true);           // LED 사용
            channel.setDescription("할 일 미완료 알림"); // 채널 설명

            notificationManager.createNotificationChannel(channel);
        }
    }

    public void showTaskReminder(Task task) {
        // MainActivity로 이동하기 위한 Intent 생성
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // PendingIntent 생성
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                task.getId(),  // 각 태스크별로 고유한 request code 사용
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd", Locale.getDefault());
        String deadlineStr = dateFormat.format(task.getDeadline());

        // 알림 생성
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(task.getTitle())
                .setContentText("완료되지 않았습니다. 기한은 " + deadlineStr + "까지 입니다.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)    // 높은 우선순위 설정
                .setDefaults(NotificationCompat.DEFAULT_ALL)     // 기본 알림음, 진동 등 사용
                .setAutoCancel(true)                            // 클릭 시 자동으로 알림 제거
                .setContentIntent(pendingIntent);               // 클릭 시 앱 실행

        // 알림 표시
        notificationManager.notify(task.getId(), builder.build());
    }
}