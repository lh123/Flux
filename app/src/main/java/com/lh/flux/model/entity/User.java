package com.lh.flux.model.entity;

import android.os.Build;

public class User {
    private String phone;
    private String sessionID;
    private String token;
    private float totalFlux = -1;
    private float availableFlux = -1;
    private String cookie;
    private String imei;
    private String imsi;
    private String manufacturer = Build.MANUFACTURER;
    private String mode = Build.MODEL;
    private int sdk = Build.VERSION.SDK_INT;

    private boolean isLogin = false;

    public void setIsLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public float getTotalFlux() {
        return totalFlux;
    }

    public void setTotalFlux(float totalFlux) {
        this.totalFlux = totalFlux;
    }

    public float getAvailableFlux() {
        return availableFlux;
    }

    public void setAvailableFlux(float availableFlux) {
        this.availableFlux = availableFlux;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getManufacturer() {
        return manufacturer;
    }


    public String getMode() {
        return mode;
    }

    public int getSdk() {
        return sdk;
    }
}
