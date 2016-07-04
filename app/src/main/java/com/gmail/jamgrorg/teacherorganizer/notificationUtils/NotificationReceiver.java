package com.gmail.jamgrorg.teacherorganizer.notificationUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by jamgr on 14.05.2016.
 */
public class NotificationReceiver extends BroadcastReceiver{

    NotificationReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, AlarmService.class);
        service.putExtra("notificationTitle", intent.getStringExtra("notificationTitle"));
        service.putExtra("notificationText", intent.getStringExtra("notificationText"));
        service.putExtra("nId", intent.getIntExtra("nId", -1));
        context.startService(service);
    }
}
