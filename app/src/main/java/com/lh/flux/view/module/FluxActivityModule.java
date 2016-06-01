package com.lh.flux.view.module;

import com.lh.flux.ActivityScope;
import com.lh.flux.mvp.presenter.FluxPresenter;
import com.lh.flux.mvp.view.IFluxActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by liuhui on 2016/5/12.
 * FluxActivityModule
 */
@Module
public class FluxActivityModule {
    private IFluxActivity fluxActivity;

    public FluxActivityModule(IFluxActivity fluxActivity) {
        this.fluxActivity = fluxActivity;
    }

    @ActivityScope
    @Provides
    public IFluxActivity provideFluxActivity() {
        return fluxActivity;
    }

    @ActivityScope
    @Provides
    public FluxPresenter provideFluxPresenter(IFluxActivity activity) {
        return new FluxPresenter(activity);
    }
}
