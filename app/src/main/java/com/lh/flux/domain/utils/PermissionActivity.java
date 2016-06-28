package com.lh.flux.domain.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.lh.flux.R;

/**
 * Created by liuhui on 2016/6/28.
 * PermissionActivity
 */
public class PermissionActivity extends AppCompatActivity {

    public static final int PERMISSION_REQUEST_CODE = 1;
    public static final int PERMISSION_GRANTED = 2;
    public static final int PERMISSION_DENIED = 3;
    private static final String EXTRA_PERMISSIONS = "permissions";
    private static final String PACKAGE_URL_SCHEME = "package:";

    private boolean needCheck = true;
    private PermissionChecker mChecker;

    public static void startPermissionActivity(Activity activity, int requestCode, String... permissions) {
        Intent intent = new Intent();
        intent.setClass(activity, PermissionActivity.class);
        intent.putExtra(EXTRA_PERMISSIONS, permissions);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() == null || !getIntent().hasExtra(EXTRA_PERMISSIONS)) {
            throw new RuntimeException("You need call startPermissionActivity to start this activity");
        }
        setContentView(R.layout.permission_aty);
        mChecker = new PermissionChecker(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (needCheck) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setBatteryOptimization();
            }
            checkPermissions(getIntent().getStringArrayExtra(EXTRA_PERMISSIONS));
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void setBatteryOptimization() {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (!pm.isIgnoringBatteryOptimizations(getPackageName())) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse(PACKAGE_URL_SCHEME + getPackageName()));
            startActivity(intent);
        }

    }

    private void checkPermissions(String... permissions) {
        needCheck = false;
        if (mChecker.lacksPermissions(permissions)) {
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
        } else {
            allPermissionGranted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE && isAllPermissionsGranted(grantResults)) {
            allPermissionGranted();
        } else {
            showMissingPermissionDialog();
        }
    }

    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("帮助");
        builder.setMessage("缺少必要权限！");
        builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setResult(PERMISSION_DENIED);
                finish();
            }
        });
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                needCheck = true;
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse(PACKAGE_URL_SCHEME + getPackageName()));
                startActivity(intent);
            }
        });
        builder.show();
    }

    private boolean isAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    private void allPermissionGranted() {
        setResult(PERMISSION_GRANTED);
        finish();
    }
}
