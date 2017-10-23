package com.lh.flux.mvp.presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.lh.flux.domain.BusProvide;
import com.lh.flux.domain.UpdateManager;
import com.lh.flux.domain.event.WelfareServiceEvent;
import com.lh.flux.domain.utils.ServiceUtil;
import com.lh.flux.model.entity.FluxEntity;
import com.lh.flux.model.entity.LoginEntity;
import com.lh.flux.model.entity.UpdateEntity;
import com.lh.flux.model.entity.WelfareInfoEntity;
import com.lh.flux.model.utils.CookieUtil;
import com.lh.flux.model.utils.PostBodyUtil;
import com.lh.flux.model.utils.ReferUtil;
import com.lh.flux.mvp.view.IFluxActivity;
import com.lh.flux.service.WelfareService;
import com.lh.flux.view.FluxActivity;
import com.lh.flux.view.fragment.DatePickerFragment;
import com.squareup.otto.Subscribe;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

public class FluxPresenter extends BasePresenter {

    public static final int LOGINING = 0;
    public static final int LOGIN_SUCCESS = 1;
    public static final int LOGIN_FAIL = 2;

    private IFluxActivity mFluxActivity;
    private Handler mHandler;

    public FluxPresenter(IFluxActivity mFluxActivity) {
        this.mFluxActivity = mFluxActivity;
    }

    public void onCreate() {
        BusProvide.getBus().register(this);
        mHandler = new Handler();
        SharedPreferences sp = mFluxActivity.getContext().getSharedPreferences("auto_grab", Context.MODE_PRIVATE);
        checkUpdate();
        if (sp.contains("time")) {
            mFluxActivity.setWelfareServiceStatus(sp.getString("time", ""), false);
        }
        startLogin();
    }

    private void checkUpdate() {
        mFluxActivity.getUpdateManager().checkUpdate(new UpdateManager.UpdateCallBack() {
            @Override
            public void onSuccess(final UpdateEntity updateEntity, boolean isNeedUpdate) {
                if (isNeedUpdate) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mFluxActivity.getContext());
                    builder.setTitle("发现新版本！");
                    builder.setMessage("发现新版本,是否更新？");
                    builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mFluxActivity.getUpdateManager().downloadApk(updateEntity.getApkFile());
                        }
                    });
                    builder.setNegativeButton("取消", null);
                    builder.show();
                }
            }

            @Override
            public void onFail() {
                Toast.makeText(mFluxActivity.getContext(), "检查更新失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void startRefreshFlux() {
        mFluxActivity.setFluxProgressStatus(true);
        service.getFluxInfo(ReferUtil.getFluxInfoRefer(userManager.getUser()), PostBodyUtil.getPhonePostBody(userManager.getUser()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<FluxEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mFluxActivity.showToast("加载失败");
                        mFluxActivity.setFluxProgressStatus(false);
                    }

                    @Override
                    public void onNext(FluxEntity fluxEntity) {
                        if (fluxEntity.isSuccess()) {
                            float ava = fluxEntity.getData().getSumAvailable();
                            float total = fluxEntity.getData().getSumTotal();
                            String msg = "流量:总共" + total + "M 可用" + ava + "M";
                            mFluxActivity.setFlux(msg, (float) (total - ava) * 100 / total);
                            userManager.getUser().setAvailableFlux(ava);
                            userManager.getUser().setTotalFlux(total);
                            userManager.saveUser();
                        } else {
                            mFluxActivity.showToast(fluxEntity.getMsg());
                        }
                        mFluxActivity.setFluxProgressStatus(false);
                    }
                });
    }

    public void startRefreshWelfareInfo() {
        mFluxActivity.setWelfareProgressStatus(true);
        if (userManager.getUser().getCookie() == null) {
            service.getWelfareCookie(userManager.getUser().getPhone(), userManager.getUser().getSessionID())
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            String cookie = response.headers().get("Set-Cookie");
                            cookie = CookieUtil.decodeCookie(cookie);
                            userManager.getUser().setCookie(cookie);
                            userManager.saveUser();
                            service.getWelfareInfo(ReferUtil.getFluxInfoRefer(userManager.getUser()), cookie)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .doOnTerminate(new Action0() {
                                        @Override
                                        public void call() {
                                            mFluxActivity.setWelfareProgressStatus(false);
                                        }
                                    })
                                    .subscribe(new Subscriber<WelfareInfoEntity>() {
                                        @Override
                                        public void onCompleted() {
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            mFluxActivity.showToast("获取信息失败");
                                        }

                                        @Override
                                        public void onNext(WelfareInfoEntity welfareEnvelopEntity) {
                                            WelfareInfoReceive(welfareEnvelopEntity);
                                        }
                                    });
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            mFluxActivity.showToast("网络错误,获取Cookie失败！");
                            mFluxActivity.setWelfareProgressStatus(false);
                        }
                    });
        } else {
            service.getWelfareInfo(ReferUtil.getFluxInfoRefer(userManager.getUser()), userManager.getUser().getCookie())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnTerminate(new Action0() {
                        @Override
                        public void call() {
                            mFluxActivity.setWelfareProgressStatus(false);
                        }
                    })
                    .subscribe(new Subscriber<WelfareInfoEntity>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            mFluxActivity.showToast("获取信息失败");
                        }

                        @Override
                        public void onNext(WelfareInfoEntity welfareEnvelopEntity) {
                            WelfareInfoReceive(welfareEnvelopEntity);
                        }
                    });
        }
    }

    public void startGrabWelfare() {
        Intent i = new Intent();
        i.setClass(mFluxActivity.getContext().getApplicationContext(), WelfareService.class);
        i.putExtra("mode", WelfareService.START_GRAB);
        mFluxActivity.getContext().getApplicationContext().startService(i);
    }

    public void startLogin() {
        if (!userManager.canLogin()) {
            return;
        }
        userManager.refreshUser();
        if (!userManager.getUser().isLogin()) {
            if (userManager.getUser().getSessionID() != null) {
                mFluxActivity.setLoginStatus(LOGINING);
                service.loginWithSessionID(PostBodyUtil.getLoginPostBodyWithSessionID(userManager.getUser()))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<LoginEntity>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                mFluxActivity.setLoginStatus(LOGIN_FAIL);
                                mFluxActivity.showToast("请检查网络连接");
                            }

                            @Override
                            public void onNext(LoginEntity loginEntity) {
                                LoginReceive(loginEntity);
                            }
                        });
                if (userManager.getUser().getTotalFlux() != -1) {
                    float ava = userManager.getUser().getAvailableFlux();
                    float total = userManager.getUser().getTotalFlux();
                    String msg = "流量:总共" + total + "M 可用" + ava + "M";
                    mFluxActivity.setFlux(msg, (float) (total - ava) * 100 / total);
                }
            } else {
                mFluxActivity.showToast("未登录");
            }
        } else {
            mFluxActivity.showToast("登录成功");
            mFluxActivity.setPhoneNum(userManager.getUser().getPhone());
            mFluxActivity.setLoginStatus(LOGIN_SUCCESS);
            userManager.saveUser();
        }

    }

    public void startGrabWelfareAtTime() {
        DatePickerFragment f = new DatePickerFragment();
        f.show(((FluxActivity) mFluxActivity).getSupportFragmentManager(), DatePickerFragment.TAG);
    }

    public void stopWelfareService() {
        if (ServiceUtil.isWelfareServiceRunning(mFluxActivity.getContext())) {
            Intent i = new Intent();
            i.setClass(mFluxActivity.getContext().getApplicationContext(), WelfareService.class);
            i.putExtra("mode", WelfareService.STOP_SERVICE);
            mFluxActivity.getContext().getApplicationContext().startService(i);
        }
    }

    @Subscribe
    public void LoginReceive(LoginEntity entity) {
        mFluxActivity.setLoginStatus(entity.isSuccess() ? LOGIN_SUCCESS : LOGIN_FAIL);
        userManager.getUser().setIsLogin(entity.isSuccess());
        if (entity.isSuccess()) {
            mFluxActivity.showToast(entity.isSuccess() ? "登录成功" : "登录失败");
            mFluxActivity.setPhoneNum(userManager.getUser().getPhone());
            userManager.getUser().setToken(entity.getToken());
            userManager.getUser().setSessionID(entity.getSessionID());
            userManager.saveUser();
        } else {
            mFluxActivity.showToast(entity.getMsg());
        }
    }

    public void WelfareInfoReceive(WelfareInfoEntity entity) {
        mFluxActivity.showToast("刷新成功");
        int total = entity.getData().getRule().getTotalNum();
        int remain = entity.getData().getRemain();
        String time = entity.isGrabing() ? "结束时间:" + entity.getData().getStartTime() : "开始时间:" + entity.getData().getStartTime();
        String type = "类型:" + entity.getData().getRule().getPrize().get(0).getData().getName();
        String num = "红包状态:剩余" + remain + "个 总共:" + total + "个";
        mFluxActivity.setWelfareInfo(num, time, type);
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onWelfareServiceEventReceive(WelfareServiceEvent event) {
        mFluxActivity.setWelfareServiceStatus(event.getMsg(), event.isGrabing());
    }

//    private boolean isWelfareServiceRunning() {
//        boolean isRunning = false;
//        ActivityManager am = (ActivityManager) mFluxActivity.getContext().getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
//        List<ActivityManager.RunningServiceInfo> list = am.getRunningServices(Integer.MAX_VALUE);
//        for (int i = 0; i < list.size(); i++) {
//            if (WelfareService.class.getName().equals(list.get(i).service.getClassName())) {
//                isRunning = true;
//                break;
//            }
//        }
//        return isRunning;
//    }

    public void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        BusProvide.getBus().unregister(this);
//        stopWelfareService();
    }
}
