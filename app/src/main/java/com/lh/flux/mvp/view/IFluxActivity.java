package com.lh.flux.mvp.view;

import android.content.Context;

import com.lh.flux.domain.UpdateManager;

public interface IFluxActivity {

    void showToast(String msg);

    void setPhoneNum(String phone);

    void setLoginStatus(int status);

    void setFluxProgressStatus(boolean status);

    void setFlux(String msg, float rate);

    void setWelfareProgressStatus(boolean status);

    void setWelfareInfo(String num, String time, String type);

    void setWelfareServiceStatus(String msg, boolean isGrabing);

    UpdateManager getUpdateManager();

    Context getContext();
}
