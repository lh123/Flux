package com.lh.flux.model.utils;

import com.lh.flux.model.entity.User;

/**
 * Created by liuhui on 2016/5/12.
 * ReferUtil
 */
public class ReferUtil
{
    public static String getWelfareRecordRefer(User user)
    {
        return "http://game.hb189.mobi/redEnvelopes/myPrize_android?phone=" + user.getPhone() + "&sessionId=" + user.getSessionID();
    }

    public static String getGrabWelfareRefer(User user)
    {
        return "http://game.hb189.mobi/welfare_android?phone=" + user.getPhone() + "&sessionId=" + user.getSessionID();
    }

    public static String getFluxInfoRefer(User user)
    {
        return "http://game.hb189.mobi/android-personal-center?phone=" + user.getPhone() + "&sessionId=" + user.getSessionID();
    }

}
