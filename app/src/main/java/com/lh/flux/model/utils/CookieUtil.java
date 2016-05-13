package com.lh.flux.model.utils;

/**
 * Created by liuhui on 2016/5/12.
 * cookieUtils
 */
public class CookieUtil
{
    public static String decodeCookie(String cookie)
    {
        if(cookie!=null)
        {
            String[] split = cookie.split(";");
            if (split.length>0)
            {
                return split[0];
            }
        }
        return null;
    }
}
