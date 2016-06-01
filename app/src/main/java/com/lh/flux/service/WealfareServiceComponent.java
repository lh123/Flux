package com.lh.flux.service;

import com.lh.flux.FluxAppComponent;
import com.lh.flux.ServiceScope;

import dagger.Component;

/**
 * Created by liuhui on 2016/5/12.
 * WealfareServiceComponent
 */
@ServiceScope
@Component(dependencies = {FluxAppComponent.class})
public interface WealfareServiceComponent {
    void inject(WelfareService service);
}
