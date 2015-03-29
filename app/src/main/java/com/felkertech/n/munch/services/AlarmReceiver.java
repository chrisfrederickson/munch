package com.felkertech.n.munch.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.felkertech.n.munch.Utils.Constants;

import java.util.Calendar;

/**
 * Created by N on 3/20/2015.
 */
public class AlarmReceiver extends BroadcastReceiver {
    public String TAG = "weather::alarm";
    public AlarmReceiver() {

    }
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent2 = new Intent(context, NotificationService.class);
        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent2.putExtra("meal", intent.getStringExtra("MEAL"));
        context.startService(intent2);
    }

    public void setAlarm(Context context){
//        Log.d(TAG, "Alarm Set for " + Constants.getInterval(context) + " hours!");

        // get a Calendar object with current time
        Calendar cal = Calendar.getInstance();
        // add 30 seconds to the calendar object
//        cal.add(Calendar.HOUR, 1);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 192837, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        // Get the AlarmManager service
        AlarmManager am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        //am.cancel(sender);
//        Log.d(TAG, 1000*60*60*Constants.getInterval(context)+"");
//        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis()+1000*60*30, 1000*60*60*Constants.getInterval(context), sender);
//        am.setExact();

        // Set the alarm to start at approximately 6 a.m.
        intent.putExtra(NotificationService.MEAL_KEY, NotificationService.BREAKFAST);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 6);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, sender);

        // Set the alarm to start at approximately 11 a.m.
        intent.putExtra(NotificationService.MEAL_KEY, NotificationService.LUNCH);
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 11);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, sender);

        // Set the alarm to start at approximately 4 p.m.
        intent.putExtra(NotificationService.MEAL_KEY, NotificationService.DINNER);
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 16);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, sender);

    }
}
