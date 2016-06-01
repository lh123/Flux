package com.lh.flux.mvp.view;

public interface ILoginActivity {
    void showToast(String msg);

    String getPhone();

    void setPhone(String phone);

    String getCap();

    void setLoginResult(int resultCode, String phone);

    void finish();
}
