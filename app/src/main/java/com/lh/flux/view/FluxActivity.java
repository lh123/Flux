package com.lh.flux.view;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lh.flux.R;
import com.lh.flux.domain.UpdateManager;
import com.lh.flux.domain.utils.PermissionActivity;
import com.lh.flux.domain.utils.PermissionChecker;
import com.lh.flux.domain.utils.ServiceUtil;
import com.lh.flux.domain.utils.ThemeUtil;
import com.lh.flux.mvp.presenter.FluxPresenter;
import com.lh.flux.mvp.view.IFluxActivity;
import com.lh.flux.view.component.DaggerFluxActivityComponent;
import com.lh.flux.view.module.FluxActivityModule;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FluxActivity extends BaseActivity implements View.OnClickListener, IFluxActivity {

    public static final int LOGIN_REQUSET_CODE = 1;
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public static int PERMISSION_REQUEST_CODE = 2;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_auto_grab_welfare)
    Button btnAutoGrabWelfare;
    @BindView(R.id.btn_refresh_flux)
    Button btnRefreshFlux;
    @BindView(R.id.btn_refresh_welfare)
    Button btnRefreshWelfare;
    @BindView(R.id.btn_grab)
    Button btnGrabWelfare;
    @BindView(R.id.btn_welfare_record)
    Button btnWelfareRecord;
    @BindView(R.id.btn_login_retry)
    Button btnLoginRety;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_login_status)
    TextView tvLoginStatus;
    @BindView(R.id.tv_flux)
    TextView tvFlux;
    @BindView(R.id.tv_flux_detail)
    TextView tvFluxDetail;
    @BindView(R.id.tv_welfare_status)
    TextView tvWelfareStatus;
    @BindView(R.id.tv_next_time)
    TextView tvNextTime;
    @BindView(R.id.tv_welfare_service_status)
    TextView tvWelfareServiceStatus;
    @BindView(R.id.tv_welfare_type)
    TextView tvWelfareType;
    @BindView(R.id.pb_flux)
    ProgressBar pbFlux;
    @BindView(R.id.pb_welfare_service_status)
    ProgressBar pbWelfareServiceStatus;
    @BindView(R.id.pb_refresh_flux)
    ProgressBar pbRefreshFlux;
    @BindView(R.id.pb_refresh_welfare)
    ProgressBar pbRefreshWelfare;
    @BindView(R.id.pb_login_status)
    ProgressBar pbLoginStatus;
    @Inject
    FluxPresenter mPresenter;
    @Inject
    UpdateManager updateManager;

    private long backFirst = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtil.getInstance().setTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flux_aty);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mPresenter.onCreate();
        btnLogin.setOnClickListener(this);
        btnRefreshFlux.setOnClickListener(this);
        btnRefreshWelfare.setOnClickListener(this);
        btnGrabWelfare.setOnClickListener(this);
        btnWelfareRecord.setOnClickListener(this);
        btnAutoGrabWelfare.setOnClickListener(this);
        btnLoginRety.setOnClickListener(this);
        PermissionChecker mChecker = new PermissionChecker(this);
        if (mChecker.lacksPermissions(PERMISSIONS)) {
            PermissionActivity.startPermissionActivity(this, PERMISSION_REQUEST_CODE, PERMISSIONS);
        }
    }

    @Override
    protected void setUpComponent() {
        DaggerFluxActivityComponent.builder()
                .fluxAppComponent(getAppComponent())
                .fluxActivityModule(new FluxActivityModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onClick(View p1) {
        switch (p1.getId()) {
            case R.id.btn_login:
                startLoginActivity();
                break;
            case R.id.btn_refresh_flux:
                mPresenter.startRefreshFlux();
                break;
            case R.id.btn_refresh_welfare:
                mPresenter.startRefreshWelfareInfo();
                break;
            case R.id.btn_grab:
                mPresenter.startGrabWelfare();
                break;
            case R.id.btn_welfare_record:
                startWelfareRecordActivity();
                break;
            case R.id.btn_auto_grab_welfare:
                mPresenter.startGrabWelfareAtTime();
                break;
            case R.id.btn_login_retry:
                mPresenter.startLogin();
                break;
        }
    }

    private void startLoginActivity() {
        Intent i = new Intent();
        i.setClass(this, LoginActivity.class);
        startActivityForResult(i, LOGIN_REQUSET_CODE);
    }

    private void startWelfareRecordActivity() {
        Intent i = new Intent();
        i.setClass(this, WelfareRecordActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting:
                Intent i = new Intent();
                i.setClass(FluxActivity.this, SettingActivity.class);
                startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPhoneNum(String phone) {
        tvPhone.setText(String.format("%s", getString(R.string.phone) + phone));
    }

    @Override
    public void setLoginStatus(int status) {
        boolean isLogin = false;
        if (status == FluxPresenter.LOGINING) {
            isLogin = false;
            tvLoginStatus.setText(R.string.logining);
            pbLoginStatus.setVisibility(View.VISIBLE);
            btnLoginRety.setVisibility(View.INVISIBLE);
        } else if (status == FluxPresenter.LOGIN_SUCCESS) {
            isLogin = true;
            tvLoginStatus.setText(R.string.login_already);
            pbLoginStatus.setVisibility(View.INVISIBLE);
            btnLoginRety.setVisibility(View.INVISIBLE);
        } else if (status == FluxPresenter.LOGIN_FAIL) {
            pbLoginStatus.setVisibility(View.INVISIBLE);
            tvLoginStatus.setText(R.string.login_fail);
            btnLoginRety.setVisibility(View.VISIBLE);
        }
        btnRefreshFlux.setEnabled(isLogin);
        btnRefreshWelfare.setEnabled(isLogin);
        btnGrabWelfare.setEnabled(isLogin);
        btnWelfareRecord.setEnabled(isLogin);
    }

    @Override
    public void setFluxProgressStatus(boolean status) {
        pbRefreshFlux.setVisibility(status ? View.VISIBLE : View.INVISIBLE);
        btnRefreshFlux.setVisibility(status ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public void setFlux(String msg, float rate) {
        tvFluxDetail.setText(msg);
        tvFlux.setText(String.format(Locale.getDefault(), "%.1f%%", rate));
        pbFlux.setProgress((int) rate);
    }

    @Override
    public void setWelfareProgressStatus(boolean status) {
        pbRefreshWelfare.setVisibility(status ? View.VISIBLE : View.INVISIBLE);
        btnRefreshWelfare.setVisibility(status ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public void setWelfareInfo(String num, String time, String type) {
        tvWelfareStatus.setText(num);
        tvNextTime.setText(time);
        tvWelfareType.setText(type);
    }

    @Override
    public void setWelfareServiceStatus(String msg, boolean isGrabing) {
        tvWelfareServiceStatus.setText(String.format("%s%s", getString(R.string.current_status), msg));
        pbWelfareServiceStatus.setVisibility(isGrabing ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public UpdateManager getUpdateManager() {
        return updateManager;
    }

    @Override
    public Context getContext() {
        return this;
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
        mPresenter.onDestroy();
        super.onDestroy();
    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        boolean result = PermissionUtil.getInstance().onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (!result) {
////            Toast.makeText(this,"部分权限被拒绝，程序可能无法正常运行",Toast.LENGTH_SHORT).show();
//            new AlertDialog.Builder(this).setTitle("警告").setMessage("部分权限被拒绝，程序可能无法正常运行").setPositiveButton("确定", null).show();
//        } else {
//            LogUtil.getInstance().init(getApplicationContext());
//            mPresenter.startLogin();
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOGIN_REQUSET_CODE) {
            if (resultCode == LoginActivity.LOGIN_SUCCESS) {
                setPhoneNum(data.getStringExtra("phone"));
                setLoginStatus(FluxPresenter.LOGIN_SUCCESS);
            } else if (resultCode == LoginActivity.LOGIN_FAIL) {
                setLoginStatus(FluxPresenter.LOGIN_FAIL);
            }
        } else if (requestCode == PERMISSION_REQUEST_CODE) {
            if (resultCode == PermissionActivity.PERMISSION_DENIED) {
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        long backSec;
        if (backFirst < 0) {
            backFirst = System.currentTimeMillis();
            showToast("再按一次退出");
        } else {
            backSec = System.currentTimeMillis();
            if (backSec - backFirst < 1000) {
                if(ServiceUtil.isWelfareServiceRunning(getApplicationContext())){
                    showServiceRunningDialoag();
                }else {
                    finish();
                }
            } else {
                backFirst = System.currentTimeMillis();
                showToast("再按一次退出");
            }
        }
    }

    private void showServiceRunningDialoag() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("是否结束红包服务");
        builder.setMessage("当前服务正在运行，是否关闭服务?");
        builder.setPositiveButton("关闭服务", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPresenter.stopWelfareService();
                finish();
            }
        });
        builder.setNegativeButton("继续后台运行", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.show();
    }
}