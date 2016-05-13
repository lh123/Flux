package com.lh.flux.view.component;

import android.app.Activity;

import com.lh.flux.ActivityScope;
import com.lh.flux.FluxAppComponent;
import com.lh.flux.view.WelfareRecordActivity;
import com.lh.flux.view.module.WelfareRecordActivityModule;

import dagger.Component;

/**
 * Created by liuhui on 2016/5/12.
 * WelfareRecordActivityComponent
 */
@ActivityScope
@Component(modules = WelfareRecordActivityModule.class,dependencies = FluxAppComponent.class)
public interface WelfareRecordActivityComponent
{
    void inject(WelfareRecordActivity activity);
}
