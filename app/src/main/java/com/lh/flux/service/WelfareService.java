package com.lh.flux.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.lh.flux.FluxApp;
import com.lh.flux.R;
import com.lh.flux.crash.LogUtil;
import com.lh.flux.domain.BusProvide;
import com.lh.flux.domain.FluxUserManager;
import com.lh.flux.domain.event.WelfareServiceEvent;
import com.lh.flux.model.api.FluxApiService;
import com.lh.flux.model.entity.GrabInfoEntity;
import com.lh.flux.model.entity.LoginEntity;
import com.lh.flux.model.utils.CookieUtil;
import com.lh.flux.model.utils.PostBodyUtil;
import com.lh.flux.model.utils.ReferUtil;
import com.lh.flux.view.FluxActivity;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WelfareService extends Service
{
    private Handler mHandler;

    private long startTime, durTime;
    private long intervalTime = 2000, actTime;
    private int loginTimes = 3;

    private boolean isAlive = false;
    private boolean isGrabWelfare = false;

    private PowerManager.WakeLock lock;
    private WifiManager.WifiLock wlock;
    private NotificationManager nm;

    public static final int START_GRAB = 0;
    public static final int START_GRAB_DELY = 1;
    public static final int STOP_SERVICE = 2;
    public static final int FLAG_SERVICE_RUNNING = 0;
    public static final int FLAG_WELFARE_RESULT = 1;
    public static final String LOCK_TAG = "auto_grab_welfare";

    @Inject
    FluxApiService service;
    @Inject
    FluxUserManager userManager;

    @Override
    public void onCreate()
    {
        super.onCreate();
        setUpComponent();
        LogUtil.getInstance().init(getApplicationContext());
        Log("service creat");
        mHandler = new Handler();
        PowerManager pw = (PowerManager) getSystemService(POWER_SERVICE);
        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        lock = pw.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, LOCK_TAG);
        wlock = wm.createWifiLock(WifiManager.WIFI_MODE_FULL, LOCK_TAG);
        lock.setReferenceCounted(false);
        wlock.setReferenceCounted(false);
        lock.acquire();
        wlock.acquire();
        intervalTime = Long.parseLong(sp.getString("interval_time", "2")) * 1000L;
        loginTimes = Integer.parseInt(sp.getString("retry_times", "3"));
        durTime = Long.parseLong(sp.getString("time_out", "3")) * 60 * 1000L;
        Log("lock cpu wifi");
        isAlive = true;
    }

    private void setUpComponent()
    {
        DaggerWealfareServiceComponent.builder()
                .fluxAppComponent(FluxApp.getApp().getAppComponent())
                .build()
                .inject(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        int mode = intent.getIntExtra("mode", -1);
        if (mode == STOP_SERVICE)
        {
            releaseLockAndStop();
        }
        else if (mode == START_GRAB || mode == START_GRAB_DELY)
        {
            if (!isGrabWelfare)
            {
                Log("service start");
                startTime = System.currentTimeMillis();
                actTime = intent.getLongExtra("act", startTime);
                Log("startTime=" + startTime);
                Log("actTime=" + actTime);
                if (mode == START_GRAB_DELY)
                {
                    startAutoGrabWithDely();
                }
                else
                {
                    startAutoGrab();
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(), "服务已在运行", Toast.LENGTH_SHORT).show();
            }
        }
        return START_NOT_STICKY;
    }

    private void startAutoGrabWithDely()
    {
        isGrabWelfare = true;
        showNotification("正在等待", FLAG_SERVICE_RUNNING);
        SharedPreferences sp = getSharedPreferences("auto_grab", Context.MODE_PRIVATE);
        sp.edit().putString("time", null).apply();
        long delyTime = actTime - startTime;
        Log("dely=" + delyTime);
        if (delyTime > 0)
        {
            mHandler.postDelayed(new Runnable()
            {

                @Override
                public void run()
                {
                    Log("after delay");
                    startAutoGrab();
                }
            }, delyTime);
        }
        else
        {
            startAutoGrab();
        }
    }

    private void startAutoGrab()
    {
        if (userManager.canLogin())
        {
            isGrabWelfare = true;
            if (!userManager.getUser().isLogin())
            {
                showNotification("正在尝试登录", FLAG_SERVICE_RUNNING);
                userManager.refreshUser();
                service.loginWithSessionID(PostBodyUtil.getLoginPostBodyWithSessionID(userManager.getUser()))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<LoginEntity>()
                        {
                            @Override
                            public void onCompleted()
                            {

                            }

                            @Override
                            public void onError(Throwable e)
                            {

                                loginTimes--;
                                if(loginTimes>0)
                                {
                                    startAutoGrab();
                                }
                                else
                                {
                                    showNotification("登陆失败", FLAG_SERVICE_RUNNING);
                                    releaseLockAndStop();
                                }
                            }

                            @Override
                            public void onNext(LoginEntity loginEntity)
                            {
                                userManager.getUser().setIsLogin(loginEntity.isSuccess());
                                if (loginEntity.isSuccess())
                                {
                                    userManager.getUser().setToken(loginEntity.getToken());
                                    userManager.getUser().setSessionID(loginEntity.getSessionID());
                                    userManager.saveUser();
                                    startAutoGrab();
                                }
                            }
                        });
            }
            else if (userManager.getUser().getCookie() == null)
            {
                showNotification("正在获取Cookie", FLAG_SERVICE_RUNNING);
                service.getWelfareCookie(userManager.getUser().getPhone(),userManager.getUser().getSessionID())
                        .enqueue(new Callback<ResponseBody>()
                        {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
                            {
                                onWelfareCookieReceive(response);
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t)
                            {
                                showNotification("获取Cookie失败", FLAG_WELFARE_RESULT);
                                releaseLockAndStop();
                            }
                        });
            }
            else
            {
                service.grabWelfare(ReferUtil.getGrabWelfareRefer(userManager.getUser()),userManager.getUser().getCookie())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<GrabInfoEntity>()
                        {
                            @Override
                            public void onCompleted()
                            {

                            }

                            @Override
                            public void onError(Throwable e)
                            {
                                showNotification("抓取红包失败,正在重试", FLAG_SERVICE_RUNNING);
                                grabWelfareDelay();
                            }

                            @Override
                            public void onNext(GrabInfoEntity grabInfoEntity)
                            {
                                onGrabEventReceive(grabInfoEntity);
                            }
                        });
            }
        }
        else
        {
            showNotification("权限被禁止，无法登陆", FLAG_WELFARE_RESULT);
            releaseLockAndStop();
        }
    }

    @Override
    public IBinder onBind(Intent p1)
    {
        return null;
    }

    private void showNotification(String msg, int mode)
    {
        updateTvText(msg);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("服务运行中");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        if (mode == FLAG_SERVICE_RUNNING)
        {
            builder.setContentText(msg);
            Notification no = builder.build();
            no.flags = Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
            nm.notify(FLAG_SERVICE_RUNNING, no);
        }
        else
        {
            builder.setContentTitle("服务已关闭");
            builder.setContentText(msg);
            Intent i = new Intent(this, FluxActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pi = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pi);
            Notification no = builder.build();
            no.flags = Notification.FLAG_AUTO_CANCEL;
            nm.notify(FLAG_WELFARE_RESULT, no);
        }
    }

    public void onGrabEventReceive(GrabInfoEntity entity)
    {
            if ("000".equals(entity.getReturnCode()))
            {
                isGrabWelfare = false;
                showNotification("抢到红包", FLAG_WELFARE_RESULT);
                releaseLockAndStop();
            }
            else if ("001".equals(entity.getReturnCode()))
            {
                showNotification("正在重新获取Cookie", FLAG_SERVICE_RUNNING);
                userManager.getUser().setCookie(null);
                grabWelfareDelay();
            }
            else if ("002".equals(entity.getReturnCode()))
            {
                showNotification("正在抢红包", FLAG_SERVICE_RUNNING);
                grabWelfareDelay();
            }
            else if ("003".equals(entity.getReturnCode()))
            {
                isGrabWelfare = false;
                showNotification("每天只能抢一次红包", FLAG_WELFARE_RESULT);
                releaseLockAndStop();
            }
            else if ("004".equals(entity.getReturnCode()))
            {
                isGrabWelfare = false;
                showNotification("没抢到红包", FLAG_WELFARE_RESULT);
                releaseLockAndStop();
            }
    }

    private void onWelfareCookieReceive(Response response)
    {
        String cookie= CookieUtil.decodeCookie(response.headers().get("Set-Cookie"));
        if (cookie!=null)
        {
            showNotification("成功获取Cookie", FLAG_SERVICE_RUNNING);
            userManager.getUser().setCookie(cookie);
            userManager.saveUser();
            startAutoGrab();
        }
    }

    private void updateTvText(String msg)
    {
        WelfareServiceEvent e = new WelfareServiceEvent(msg, isGrabWelfare);
        sendToPresenter(e);
    }

    private void grabWelfareDelay()
    {
        if (System.currentTimeMillis() - actTime > durTime)
        {
            isGrabWelfare = false;
            Log("超时");
            showNotification("超时", FLAG_WELFARE_RESULT);
            releaseLockAndStop();
        }
        else
        {
            mHandler.postDelayed(new Runnable()
            {

                @Override
                public void run()
                {
                    if (isAlive)
                    {
                        startAutoGrab();
                    }
                }
            }, intervalTime);
        }
    }

    private void Log(String msg)
    {
        LogUtil.getInstance().logE("WelfareService", msg);

    }

    private void releaseLockAndStop()
    {
        if (lock != null && lock.isHeld())
        {
            lock.release();
            lock = null;
            Log("unlock cpu");
        }
        if (wlock != null && wlock.isHeld())
        {
            wlock.release();
            wlock = null;
            Log("unlock wifi");
        }
        stopSelf();
    }

    private void sendToPresenter(final Object o)
    {
        if (Looper.myLooper() != Looper.getMainLooper())
        {
            mHandler.post(new Runnable()
            {

                @Override
                public void run()
                {
                    BusProvide.getBus().post(o);
                }
            });
        }
        else
        {
            BusProvide.getBus().post(o);
        }
    }

    @Override
    public void onDestroy()
    {
        isAlive = false;
        nm.cancel(FLAG_SERVICE_RUNNING);
        BusProvide.getBus().unregister(this);
        mHandler.removeCallbacksAndMessages(null);
        Log("service destroy");
        super.onDestroy();
        FluxApp.getRefWatcher(this).watch(this, "service");
    }
}
