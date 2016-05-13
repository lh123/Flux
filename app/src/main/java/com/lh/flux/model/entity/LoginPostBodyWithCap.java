package com.lh.flux.model.entity;

/**
 * Created by liuhui on 2016/5/12.
 * LoginPostBodyWithCap
 */
@SuppressWarnings("unused")
public class LoginPostBodyWithCap
{// "{\"SDK\":" + u.getSdk() + ",\"captcha\":\"" + cap + "\",\"channel\":\"default\",\"data\":{},\"imei\":\"" + u.getImei() + "\",\"imsi\":\"" + u.getImsi() + "\",\"isIOS\":\"false\",\"isPreInstalled\":false,\"manufacturer\":\"" + u.getManufacturer() + "\",\"mode\":\"" + u.getMode() + "\",\"userPhone\":\"" + u.getPhone() + "\"}";
    private int SDK;
    private String captcha;
    private String channel="default";
    private Object data;
    private String imei;
    private String imsi;
    private String isIOS="false";
    private boolean isPreInstalled=false;
    private String manufacturer;
    private String mode;
    private String userPhone;

    public String getUserPhone()
    {
        return userPhone;
    }

    public void setUserPhone(String userPhone)
    {
        this.userPhone = userPhone;
    }

    public int getSDK()
    {
        return SDK;
    }

    public void setSDK(int SDK)
    {
        this.SDK = SDK;
    }

    public String getCaptcha()
    {
        return captcha;
    }

    public void setCaptcha(String captcha)
    {
        this.captcha = captcha;
    }

    public String getChannel()
    {
        return channel;
    }

    public void setChannel(String channel)
    {
        this.channel = channel;
    }

    public Object getData()
    {
        return data;
    }

    public void setData(Object data)
    {
        this.data = data;
    }

    public String getImei()
    {
        return imei;
    }

    public void setImei(String imei)
    {
        this.imei = imei;
    }

    public String getImsi()
    {
        return imsi;
    }

    public void setImsi(String imsi)
    {
        this.imsi = imsi;
    }

    public String getIsIOS()
    {
        return isIOS;
    }

    public void setIsIOS(String isIOS)
    {
        this.isIOS = isIOS;
    }

    public boolean isPreInstalled()
    {
        return isPreInstalled;
    }

    public void setPreInstalled(boolean preInstalled)
    {
        isPreInstalled = preInstalled;
    }

    public String getManufacturer()
    {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer)
    {
        this.manufacturer = manufacturer;
    }

    public String getMode()
    {
        return mode;
    }

    public void setMode(String mode)
    {
        this.mode = mode;
    }


}
