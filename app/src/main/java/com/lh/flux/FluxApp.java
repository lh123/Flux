package com.lh.flux;

import android.app.Application;
import android.content.Context;
import android.os.UserManager;
import android.util.Log;

import com.lh.flux.crash.LogUtil;
import com.lh.flux.crash.MyCrashHandler;
import com.lh.flux.domain.utils.ThemeUtil;
import com.lh.flux.model.api.FluxApiModule;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.bugly.Bugly;

import java.lang.reflect.Method;

public class FluxApp extends Application {
    private static FluxApp app;
    private RefWatcher refWatcher;
    private FluxAppComponent appComponent;

    public static FluxApp getApp() {
        return app;
    }

    public static RefWatcher getRefWatcher(Context context) {
        FluxApp application = (FluxApp) context.getApplicationContext();
        return application.refWatcher;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        trySolveLeak();
        refWatcher = LeakCanary.install(this);
        MyCrashHandler.getInstance().init(this);
        LogUtil.getInstance().init(this);
        Thread.setDefaultUncaughtExceptionHandler(MyCrashHandler.getInstance());
//        CrashReport.initCrashReport(this, "900014048", false);
        Bugly.init(this, "900014048", true);
        ThemeUtil.getInstance().init(this);
        initAppComponent();
    }

    private void initAppComponent() {
        appComponent = DaggerFluxAppComponent.builder()
                .fluxApiModule(new FluxApiModule())
                .fluxAppModule(new FluxAppModule(this))
                .build();
    }

    public FluxAppComponent getAppComponent() {
        return appComponent;
    }

    private void trySolveLeak() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            try {
                UserManager um = (UserManager) getSystemService(USER_SERVICE);
                Class<? extends UserManager> clss = um.getClass();
                Method m = clss.getDeclaredMethod("get", Context.class);
                m.invoke(um, this);
                Log.e("reflect", "success");
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("reflect", "fail");
            }
        }
    }
}
