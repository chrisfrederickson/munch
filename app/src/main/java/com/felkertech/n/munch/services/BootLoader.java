package com.felkertech.n.munch.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootLoader extends BroadcastReceiver {
    public BootLoader() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: Also implement in WeatherDelta
/*        Intent ntent = new Intent(context, NotificationService.class);
        context.startService(ntent);*/
        AlarmReceiver ar = new AlarmReceiver();
        ar.setAlarm(context);
    }
}
