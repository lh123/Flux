package com.lh.flux.mvp.presenter;

import com.lh.flux.FluxApp;
import com.lh.flux.domain.FluxUserManager;
import com.lh.flux.model.api.FluxApiService;

import javax.inject.Inject;

/**
 * Created by liuhui on 2016/5/12.
 * BasePresenter提供依赖
 */
public class BasePresenter
{
    @Inject FluxApiService service;
    @Inject FluxUserManager userManager;

    public BasePresenter()
    {
        FluxApp.getApp().getAppComponent().inject(this);
    }
}
