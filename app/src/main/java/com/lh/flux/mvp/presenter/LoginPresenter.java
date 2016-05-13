package com.lh.flux.mvp.presenter;

import com.lh.flux.model.api.FluxApiService;
import com.lh.flux.model.entity.CapEntity;
import com.lh.flux.model.entity.LoginEntity;
import com.lh.flux.model.entity.User;
import com.lh.flux.model.utils.PostBodyUtil;
import com.lh.flux.model.utils.ReferUtil;
import com.lh.flux.mvp.view.ILoginActivity;
import com.lh.flux.view.LoginActivity;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginPresenter extends BasePresenter
{
    private ILoginActivity loginAty;
    private FluxApiService service;
    private User user;

    public LoginPresenter(ILoginActivity loginAty, FluxApiService service)
    {
        this.loginAty = loginAty;
        this.service = service;
    }

    public void onCreat()
    {
        user=userManager.getUser();
        loginAty.setPhone(user.getPhone());
    }

    public void getCap()
    {
        user.setPhone(loginAty.getPhone());
        service.getCap(user.getPhone())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CapEntity>()
                {
                    @Override
                    public void onCompleted()
                    {
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        loginAty.showToast("网络错误");
                    }

                    @Override
                    public void onNext(CapEntity capEntity)
                    {
                        loginAty.showToast(capEntity.getMsg());
                    }
                });
    }

    public void login()
    {
        service.loginWithCap(PostBodyUtil.getLoginPostBodyWithCap(user,loginAty.getCap()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LoginEntity>()
                {
                    @Override
                    public void onCompleted()
                    {

                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        e.printStackTrace();
                        loginAty.showToast("网络错误");
                    }

                    @Override
                    public void onNext(LoginEntity loginEntity)
                    {
                        if(loginEntity.isSuccess())
                        {
                            user.setPhone(loginAty.getPhone());
                            user.setToken(loginEntity.getToken());
                            user.setSessionID(loginEntity.getSessionID());
                            userManager.saveUser();
                            loginAty.setLoginResult(LoginActivity.LOGIN_SUCCESS,user.getPhone());
                            loginAty.finish();
                        }
                        else
                        {
                            loginAty.showToast(loginEntity.getMsg());
                        }
                    }
                });
    }

    public void onDestroy()
    {

    }
}
