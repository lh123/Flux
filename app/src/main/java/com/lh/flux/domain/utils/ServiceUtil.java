package com.lh.flux.domain.utils;

import android.app.ActivityManager;
import android.content.Context;

import com.lh.flux.service.WelfareService;

import java.util.List;

/**
 * Created by liuhui on 2016/7/3.
 * ServiceUtil
 */
public class ServiceUtil {
    public static boolean isWelfareServiceRunning(Context context) {
        boolean isRunning = false;
        ActivityManager am = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> list = am.getRunningServices(Integer.MAX_VALUE);
        for (int i = 0; i < list.size(); i++) {
            if (WelfareService.class.getName().equals(list.get(i).service.getClassName())) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }
}
