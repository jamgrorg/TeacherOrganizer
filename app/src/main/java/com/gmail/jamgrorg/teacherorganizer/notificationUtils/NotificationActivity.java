package com.gmail.jamgrorg.teacherorganizer.notificationUtils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

/**
 * Created by jamgr on 14.05.2016.
 */
public class NotificationActivity {

    private PendingIntent pendingIntent;

    public void createNotification(Context context, int year, int month, int day,
                                int hour, int minut, String notificationTitle,
                                String notificationText) {

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minut);

        Intent intent = new Intent(context, NotificationReceiver.class); // Intent, который будет открываться по нажатию на уведомление ВРЯДЛИ
        intent.putExtra("notificationTitle", notificationTitle); // Передаем в уведомление заголовок заметки
        intent.putExtra("notificationText", notificationText); // Передаем в уведомление текст заметки
        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    public void cancelNotification(Context context) {
        Intent intent = new Intent(context, NotificationReceiver.class); // Intent, который будет открываться по нажатию на уведомление ВРЯДЛИ

        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }
}
