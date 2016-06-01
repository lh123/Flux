package com.lh.flux.domain;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;

import com.lh.flux.model.entity.User;

/**
 * Created by liuhui on 2016/5/12.
 * FluxUserManager用户管理类
 */
public class FluxUserManager {
    private User user;
    private SharedPreferences mPreference;
    private Context mContext;

    public FluxUserManager(Context context) {
        user = new User();
        this.mContext = context;
        mPreference = context.getSharedPreferences("user", Context.MODE_PRIVATE);
    }

    public boolean canLogin() {
        return ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED
                || (mPreference.contains("imei") && mPreference.contains("imsi"));
    }

    public void refreshUser() {
        if (mPreference.contains("imei") && mPreference.contains("imsi")) {
            user.setImei(mPreference.getString("imei", null));
            user.setImsi(mPreference.getString("imsi", null));
        } else {
            TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
            user.setImei(tm.getDeviceId());
            user.setImsi(tm.getSubscriberId());
        }
        user.setPhone(mPreference.getString("phone", null));
        user.setToken(mPreference.getString("token", null));
        user.setSessionID(mPreference.getString("sessionID", null));
        user.setAvailableFlux(mPreference.getInt("availableFlux", 0));
        user.setTotalFlux(mPreference.getInt("totalFlux", 0));
    }

    public void saveUser() {
        SharedPreferences.Editor edit = mPreference.edit();
        edit.putString("imei", user.getImei());
        edit.putString("imsi", user.getImsi());
        edit.putString("phone", user.getPhone());
        edit.putString("token", user.getToken());
        edit.putString("sessionID", user.getSessionID());
        edit.putInt("availableFlux", user.getAvailableFlux());
        edit.putInt("totalFlux", user.getTotalFlux());
        edit.apply();
    }

    public User getUser() {
        return user;
    }
}
