package com.lh.flux.view.module;

import com.lh.flux.ActivityScope;
import com.lh.flux.mvp.presenter.WelfareRecordPresenter;
import com.lh.flux.mvp.view.IWelfareRecordActivity;

import dagger.Module;
import dagger.Provides;
import retrofit2.http.POST;

/**
 * Created by liuhui on 2016/5/12.
 * WelfareRecordActivityModule
 */
@Module
public class WelfareRecordActivityModule
{
    private IWelfareRecordActivity welfareRecordActivity;

    public WelfareRecordActivityModule(IWelfareRecordActivity welfareRecordActivity)
    {
        this.welfareRecordActivity = welfareRecordActivity;
    }

    @ActivityScope
    @Provides
    public IWelfareRecordActivity provideWelfareRecordActivity()
    {
        return welfareRecordActivity;
    }

    @ActivityScope
    @Provides
    public WelfareRecordPresenter provideWelfareRecordPresenter()
    {
        return new WelfareRecordPresenter(welfareRecordActivity);
    }
}
