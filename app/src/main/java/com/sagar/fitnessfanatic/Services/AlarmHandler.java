package com.sagar.fitnessfanatic.Services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class AlarmHandler {
    private Context context;

    public AlarmHandler(Context context){
        this.context = context;
    }

    public void setAlarmManager(){
        Intent intent = new Intent(context, ExecutableServices.class);
        PendingIntent sender = PendingIntent.getBroadcast(context,2,intent,0);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (am != null){
            long triggerAfter = 1 * 60 * 1000;
            long triggerEvery = 1 * 60 * 1000;
            am.setRepeating(AlarmManager.RTC_WAKEUP,triggerAfter, triggerEvery, sender);
        }
    }

    public void cancelAlarmManager(){
        Intent intent = new Intent(context, ExecutableServices.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 2, intent ,0);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (am!=null){
            am.cancel(sender);
        }
    }
}
