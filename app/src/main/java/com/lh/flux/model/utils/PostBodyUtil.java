package com.lh.flux.model.utils;

import com.lh.flux.model.entity.LoginPostBodyWithCap;
import com.lh.flux.model.entity.LoginPostBodyWithSessionID;
import com.lh.flux.model.entity.PhonePostBody;
import com.lh.flux.model.entity.User;

/**
 * Created by liuhui on 2016/5/12.
 * PostBodyUtil
 */
public class PostBodyUtil {
    public static PhonePostBody getPhonePostBody(User user) {
        return new PhonePostBody(user.getPhone());
    }

    public static LoginPostBodyWithCap getLoginPostBodyWithCap(User user, String captcha) {
        LoginPostBodyWithCap bodyWithCap = new LoginPostBodyWithCap();
        bodyWithCap.setSDK(user.getSdk());
        bodyWithCap.setCaptcha(captcha);
        bodyWithCap.setData(new Object());
        bodyWithCap.setImei(user.getImei());
        bodyWithCap.setImsi(user.getImsi());
        bodyWithCap.setManufacturer(user.getManufacturer());
        bodyWithCap.setMode(user.getMode());
        bodyWithCap.setUserPhone(user.getPhone());
        return bodyWithCap;
    }

    public static LoginPostBodyWithSessionID getLoginPostBodyWithSessionID(User user) {
        LoginPostBodyWithSessionID bodyWithSessionID = new LoginPostBodyWithSessionID();
        bodyWithSessionID.setSDK(user.getSdk());
        bodyWithSessionID.setData(new Object());
        bodyWithSessionID.setImsi(user.getImsi());
        bodyWithSessionID.setManufacturer(user.getManufacturer());
        bodyWithSessionID.setMode(user.getMode());
        bodyWithSessionID.setSessionId(user.getSessionID());
        bodyWithSessionID.setToken(user.getToken());
        bodyWithSessionID.setUserPhone(user.getPhone());
        return bodyWithSessionID;
    }


}
