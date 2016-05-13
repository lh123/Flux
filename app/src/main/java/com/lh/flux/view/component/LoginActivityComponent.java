package com.lh.flux.view.component;

import android.app.Activity;

import com.lh.flux.ActivityScope;
import com.lh.flux.FluxAppComponent;
import com.lh.flux.view.LoginActivity;
import com.lh.flux.view.module.LoginActivityModule;

import dagger.Component;

/**
 * Created by liuhui on 2016/5/12.
 * LoginActivityComponent
 */
@ActivityScope
@Component(modules = LoginActivityModule.class,dependencies = FluxAppComponent.class)
public interface LoginActivityComponent
{
    void inject(LoginActivity activity);
}
