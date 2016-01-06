package com.lh.flux;

import android.app.Application;
import android.content.Context;
import android.os.UserManager;

import com.lh.flux.crash.MyCrashHandler;
import com.lh.flux.domain.FluxUserManager;
import com.lh.flux.domain.utils.ThemeUtil;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;

import java.lang.reflect.Method;

public class FluxApp extends Application
{
    private RefWatcher refWatcher;

    @Override
    public void onCreate()
    {
        super.onCreate();
        trySolveLeak();
        refWatcher = LeakCanary.install(this);
        FluxUserManager.getInstance().init(this);
        MyCrashHandler.getInstance().init(this);
        Thread.setDefaultUncaughtExceptionHandler(MyCrashHandler.getInstance());
        MobclickAgent.setCatchUncaughtExceptions(false);
        CrashReport.initCrashReport(this, "900014048", false);
        ThemeUtil.getInstance().init(this);
    }

    public static RefWatcher getRefWatcher(Context context)
    {
        FluxApp application = (FluxApp) context.getApplicationContext();
        return application.refWatcher;
    }

    private void trySolveLeak()
    {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1)
        {
            try
            {
                UserManager um = (UserManager) getSystemService(USER_SERVICE);
                Class<? extends UserManager> clss = um.getClass();
                Method m = clss.getDeclaredMethod("get", Context.class);
                m.invoke(um, this);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
