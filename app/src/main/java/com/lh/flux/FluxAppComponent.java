package com.lh.flux;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;

import com.lh.flux.domain.FluxUserManager;
import com.lh.flux.domain.UpdateManager;
import com.lh.flux.model.api.FluxApiModule;
import com.lh.flux.model.api.FluxApiService;
import com.lh.flux.model.entity.User;
import com.lh.flux.mvp.presenter.BasePresenter;
import com.squareup.leakcanary.RefWatcher;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by liuhui on 2016/5/12.
 * FluxAppComponent
 */
@Singleton
@Component(modules = {FluxApiModule.class, FluxAppModule.class})
public interface FluxAppComponent
{
    void inject(Activity activity);

    void inject(Fragment fragment);

    void inject(BasePresenter presenter);

    Context getContext();

    User getUser();

    FluxUserManager getUserManager();

    FluxApiService getFluxApiService();

    OkHttpClient getOkhttpClient();

    Retrofit getRetrofit();

    RefWatcher getRefWatcher();

    UpdateManager getUpdateManager();
}
