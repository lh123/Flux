package com.lh.flux.mvp.presenter;

import android.os.Handler;

import com.lh.flux.domain.BusProvide;
import com.lh.flux.model.entity.WelfareRecordEntity;
import com.lh.flux.model.utils.CookieUtil;
import com.lh.flux.model.utils.PostBodyUtil;
import com.lh.flux.model.utils.ReferUtil;
import com.lh.flux.mvp.view.IWelfareRecordActivity;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WelfareRecordPresenter extends BasePresenter
{
    private IWelfareRecordActivity mWelfareRecordActivity;

    public WelfareRecordPresenter(IWelfareRecordActivity mWelfareRecordActivity)
    {
        this.mWelfareRecordActivity = mWelfareRecordActivity;
    }

    public void onCreate()
    {
        BusProvide.getBus().register(this);
    }

    public void startRefreshWelfareRecord()
    {
        new Handler().post(new Runnable()
        {

            @Override
            public void run()
            {
                mWelfareRecordActivity.setRefreshStatus(true);
            }
        });
        if (userManager.getUser().getCookie() == null)
        {
            service.getWelfareCookie(userManager.getUser().getPhone(),userManager.getUser().getSessionID())
                    .enqueue(new Callback<ResponseBody>()
                    {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
                        {
                            userManager.getUser().setCookie(CookieUtil.decodeCookie(response.headers().get("Set-Cookie")));
                            service.getWelfareRecord(ReferUtil.getWelfareRecordRefer(userManager.getUser()),
                                    userManager.getUser().getCookie(),
                                    PostBodyUtil.getPhonePostBody(userManager.getUser()))
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Subscriber<WelfareRecordEntity>()
                                    {
                                        @Override
                                        public void onCompleted()
                                        {
                                        }

                                        @Override
                                        public void onError(Throwable e)
                                        {
                                            e.printStackTrace();
                                            mWelfareRecordActivity.showToast("网路错误");
                                            mWelfareRecordActivity.setRefreshStatus(false);
                                        }

                                        @Override
                                        public void onNext(WelfareRecordEntity welfareRecordEntity)
                                        {
                                            mWelfareRecordActivity.setRefreshStatus(false);
                                            if ( welfareRecordEntity.isSuccess())
                                            {
                                                mWelfareRecordActivity.setData(welfareRecordEntity.getData());
                                            }
                                            else
                                            {
                                                mWelfareRecordActivity.setData(new ArrayList<WelfareRecordEntity.Data>());
                                                mWelfareRecordActivity.showToast("获取失败");
                                            }
                                        }
                                    });
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t)
                        {
                            t.printStackTrace();
                            mWelfareRecordActivity.showToast("网路错误");
                            mWelfareRecordActivity.setRefreshStatus(false);
                        }
                    });

        }
        else
        {
            service.getWelfareRecord(ReferUtil.getWelfareRecordRefer(userManager.getUser()),
                    userManager.getUser().getCookie(),
                    PostBodyUtil.getPhonePostBody(userManager.getUser()))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<WelfareRecordEntity>()
                    {
                        @Override
                        public void onCompleted()
                        {
                        }

                        @Override
                        public void onError(Throwable e)
                        {
                            e.printStackTrace();
                            mWelfareRecordActivity.showToast("网路错误");
                            mWelfareRecordActivity.setRefreshStatus(false);
                        }

                        @Override
                        public void onNext(WelfareRecordEntity welfareRecordEntity)
                        {
                            mWelfareRecordActivity.setRefreshStatus(false);
                            if ( welfareRecordEntity.isSuccess())
                            {
                                mWelfareRecordActivity.setData(welfareRecordEntity.getData());
                            }
                            else
                            {
                                mWelfareRecordActivity.setData(new ArrayList<WelfareRecordEntity.Data>());
                                mWelfareRecordActivity.showToast("获取失败");
                            }
                        }
                    });
        }
    }

    public void onDestroy()
    {
        BusProvide.getBus().unregister(this);
    }
}
