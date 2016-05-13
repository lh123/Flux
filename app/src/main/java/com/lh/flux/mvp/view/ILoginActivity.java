package com.lh.flux.mvp.view;

import android.content.Intent;

public interface ILoginActivity
{
    void showToast(String msg);

    String getPhone();

    String getCap();

    void setPhone(String phone);

    void setLoginResult(int resultCode,String phone);

    void finish();
}
