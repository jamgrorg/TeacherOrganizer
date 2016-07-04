package com.gmail.jamgrorg.teacherorganizer.notificationUtils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.gmail.jamgrorg.teacherorganizer.MainActivity;
import com.gmail.jamgrorg.teacherorganizer.R;

/**
 * Created by jamgr on 14.05.2016.
 */
public class AlarmService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressWarnings("static_access")
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        NotificationManager notificationManager = (NotificationManager)
                this.getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        Intent intent1 = new Intent(this.getApplicationContext(), MainActivity.class);

        // Установить 0 для
        PendingIntent pendingIntent = PendingIntent.getActivity(this.getApplicationContext(),
                0, intent1, 0);

        Notification notification;

        Notification.Builder builder = new Notification.Builder(this);

        builder.setAutoCancel(true);
        builder.setTicker(intent.getStringExtra("notificationTitle"));
        builder.setContentTitle(intent.getStringExtra("notificationTitle"));
        builder.setContentText(intent.getStringExtra("notificationText"));
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentIntent(pendingIntent);
        builder.setOngoing(false);
        builder.setDefaults(Notification.DEFAULT_ALL);
        //builder.setSubText(String.valueOf(intent.getIntExtra("nId", -1)));
        builder.build();

        notification = builder.getNotification();
        notificationManager.notify(0, notification);
    }
}
