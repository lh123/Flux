package com.lh.flux.model.entity;

/**
 * Created by liuhui on 2016/5/12.
 * postbody
 */
public class PhonePostBody
{
    public PhonePostBody(String phone)
    {
        this.phone = phone;
    }

    //"{\"phone\":\"" + user.getPhone() + "\"}";
    private String phone;

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }
}
