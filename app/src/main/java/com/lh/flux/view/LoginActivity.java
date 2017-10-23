package com.lh.flux.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lh.flux.R;
import com.lh.flux.domain.utils.ThemeUtil;
import com.lh.flux.mvp.presenter.LoginPresenter;
import com.lh.flux.mvp.view.ILoginActivity;
import com.lh.flux.view.component.DaggerLoginActivityComponent;
import com.lh.flux.view.module.LoginActivityModule;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity implements ILoginActivity {
    public static final int LOGIN_SUCCESS = 1;
    public static final int LOGIN_FAIL = 2;
    public static final int LOGIN_CANCEL = 3;
    @Inject
    LoginPresenter presenter;
    @BindView(R.id.login_ed_phone)
    EditText edPhone;
    @BindView(R.id.login_ed_cap)
    EditText edCap;
    @BindView(R.id.login_btn_get_cap)
    Button btnGetCap;
    @BindView(R.id.login_btn_login)
    Button btnLogin;
    private boolean haveResult = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtil.getInstance().setTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_aty);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        presenter.onCreat();
        btnGetCap.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View p1) {
                presenter.getCap();
            }
        });
        btnLogin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View p1) {
                presenter.login();
            }
        });
    }

    @Override
    protected void setUpComponent() {
        DaggerLoginActivityComponent.builder()
                .loginActivityModule(new LoginActivityModule(this))
                .fluxAppComponent(getAppComponent())
                .build()
                .inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        if (!haveResult) {
            setResult(LOGIN_CANCEL);
        }
        super.onDestroy();
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public String getPhone() {
        return edPhone.getText().toString();
    }

    @Override
    public void setPhone(String phone) {
        edPhone.setText(phone);
    }

    @Override
    public String getCap() {
        return edCap.getText().toString();
    }

    @Override
    public void setLoginResult(int resultCode, String phone) {
        haveResult = true;
        Intent i = new Intent();
        i.putExtra("phone", phone);
        setResult(resultCode, i);
    }
}
