package com.lh.flux.view.module;

import com.lh.flux.ActivityScope;
import com.lh.flux.model.api.FluxApiService;
import com.lh.flux.mvp.presenter.LoginPresenter;
import com.lh.flux.mvp.view.ILoginActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by liuhui on 2016/5/12.
 * LoginActivityModule
 */
@Module
public class LoginActivityModule
{
    private ILoginActivity loginActivity;

    public LoginActivityModule(ILoginActivity loginActivity)
    {
        this.loginActivity = loginActivity;
    }

    @ActivityScope
    @Provides
    public ILoginActivity provideLoginActivity()
    {
        return loginActivity;
    }

    @ActivityScope
    @Provides
    public LoginPresenter provideLoginPresenter(FluxApiService service)
    {
        return new LoginPresenter(loginActivity, service);
    }
}
