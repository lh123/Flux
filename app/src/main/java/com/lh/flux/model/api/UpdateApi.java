package com.lh.flux.model.api;

import com.lh.flux.model.entity.UpdateEntity;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import rx.Observable;

/**
 * Created by liuhui on 2016/5/22
 * updateApi
 */
public interface UpdateApi
{
    @GET("https://api.bmob.cn/1/classes/update/2QxJeeey")
    @Headers({"X-Bmob-Application-Id: ff26ae3e640b065d0a3303f1617b16f5",
            "X-Bmob-REST-API-Key: 84e6d21ff49f162c549f936dfac3fb6e"})
    Observable<UpdateEntity> getUpdateInfo();
}
