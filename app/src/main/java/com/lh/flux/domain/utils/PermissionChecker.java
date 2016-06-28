package com.lh.flux.domain.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * Created by liuhui on 2016/6/28.
 * PermissionChecker
 */
public class PermissionChecker {
    private Context context;

    public PermissionChecker(Context context) {
        this.context = context;
    }

    public boolean lacksPermissions(String... permissions) {
        for (String permission : permissions) {
            if (lackPermissions(permission)) {
                return true;
            }
        }
        return false;
    }

    private boolean lackPermissions(String permission) {
        return ContextCompat.checkSelfPermission(context, permission) ==
                PackageManager.PERMISSION_DENIED;
    }
}
