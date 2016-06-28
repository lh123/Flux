package com.lh.flux.receiver;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import com.lh.flux.R;
import com.lh.flux.crash.LogUtil;
import com.lh.flux.service.WelfareService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by liuhui on 2016/6/1.
 * BootCompleteReceiver
 */
public class BootCompleteReceiver extends BroadcastReceiver {

    public static final int NOTIFYID = 100;

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtil.getInstance().logE("BootCompleteReceiver", "BootComplete");
        if (PreferenceManager
                .getDefaultSharedPreferences(context).getBoolean("reboot_auto_set", false)) {
            setAlarm(context);
        }
    }

    private void setAlarm(Context context) {
        SharedPreferences timeSp = context.getSharedPreferences("auto_grab", Context.MODE_PRIVATE);
        long advanceTime = Long.parseLong(timeSp.getString("advance_time", "3")) * 60 * 1000;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour < 12) {
            calendar.set(Calendar.HOUR_OF_DAY, 12);
        } else if (hour < 19) {
            calendar.set(Calendar.HOUR_OF_DAY, 19);
        } else {
            calendar.add(Calendar.DATE, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 12);
        }
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Intent i = new Intent();
        i.setClass(context, WelfareService.class);
        i.putExtra("mode", WelfareService.START_GRAB_DELY);
        i.putExtra("act", calendar.getTimeInMillis());
        PendingIntent pendingIntent = PendingIntent
                .getService(context.getApplicationContext(), 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() - advanceTime, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() - advanceTime, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() - advanceTime, pendingIntent);
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        LogUtil.getInstance().logE("BootCompleteReceiver", "auto grab " + df.format(calendar.getTime()));
        String time = df.format(calendar.getTime());
        timeSp.edit().putString("time", "自动抢红包:" + time).apply();
        sendNotify(context, "自动定时:" + time);
    }

    private void sendNotify(Context context, String msg) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle("检测到重启");
        builder.setContentText(msg);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        Notification notification = builder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(NOTIFYID, notification);
    }
}
