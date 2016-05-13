package com.lh.flux.model.entity;

/**
 * Created by liuhui on 2016/5/12.
 * LoginPostBodyWithSessionID
 */
@SuppressWarnings("unused")
public class LoginPostBodyWithSessionID
{
    //"{\"SDK\":" + u.getSdk() + ",\"channel\":\"default\",\"data\":{},\"imsi\":\"" + u.getImsi() + "\",\"isPreInstalled\":false,\"manufacturer\":\"" + u.getManufacturer() + "\",\"mode\":\"" + u.getMode() + "\",\"sessionId\":\"" + u.getSessionID() + "\",\"token\":\"" + u.getToken() + "\",\"userPhone\":\"" + u.getPhone() + "\"}";
    private int SDK;
    private String channel="default";
    private Object data;
    private String imsi;
    private boolean isPreInstalled=false;
    private String manufacturer;
    private String mode;
    private String sessionId;
    private String token;
    private String userPhone;

    public int getSDK()
    {
        return SDK;
    }

    public void setSDK(int SDK)
    {
        this.SDK = SDK;
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

    public String getImsi()
    {
        return imsi;
    }

    public void setImsi(String imsi)
    {
        this.imsi = imsi;
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

    public String getSessionId()
    {
        return sessionId;
    }

    public void setSessionId(String sessionId)
    {
        this.sessionId = sessionId;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public String getUserPhone()
    {
        return userPhone;
    }

    public void setUserPhone(String userPhone)
    {
        this.userPhone = userPhone;
    }
}
