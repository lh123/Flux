package com.lh.flux.view.component;

import com.lh.flux.ActivityScope;
import com.lh.flux.FluxAppComponent;
import com.lh.flux.view.FluxActivity;
import com.lh.flux.view.module.FluxActivityModule;

import dagger.Component;

/**
 * Created by liuhui on 2016/5/12.
 * FluxActivityComponent
 */
@ActivityScope
@Component(modules = {FluxActivityModule.class}, dependencies = {FluxAppComponent.class})
public interface FluxActivityComponent {
    void inject(FluxActivity activity);
}
