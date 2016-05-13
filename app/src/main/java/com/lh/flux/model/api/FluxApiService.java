package com.lh.flux.model.api;

import com.lh.flux.model.entity.CapEntity;
import com.lh.flux.model.entity.FluxEntity;
import com.lh.flux.model.entity.GrabInfoEntity;
import com.lh.flux.model.entity.LoginEntity;
import com.lh.flux.model.entity.LoginPostBodyWithCap;
import com.lh.flux.model.entity.LoginPostBodyWithSessionID;
import com.lh.flux.model.entity.PhonePostBody;
import com.lh.flux.model.entity.WelfareInfoEntity;
import com.lh.flux.model.entity.WelfareRecordEntity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by liuhui on 2016/5/12.
 * FluxApiService
 */
public interface FluxApiService
{
    @GET("getCaptcha/{phone}")
    Observable<CapEntity> getCap(@Path("phone") String phone);

    @POST("login")
    @Headers("Content-type: application/json; charset=utf-8")
    Observable<LoginEntity> loginWithCap(@Body LoginPostBodyWithCap body);

    @POST("login")
    @Headers("Content-type: application/json; charset=utf-8")
    Observable<LoginEntity> loginWithSessionID(@Body LoginPostBodyWithSessionID body);

    @POST("user/checkUserFlux")
    @Headers({"Origin: http://game.hb189.mobi",
            "Content-Type: application/json;charset=UTF-8"})
    Observable<FluxEntity> getFluxInfo(@Header("Referer") String refer, @Body PhonePostBody body);

    @GET("redEnvelopes/envelopesInfo")
    @Headers("Content-type: application/json; charset=utf-8")
    Observable<WelfareInfoEntity> getWelfareInfo(@Header("Referer")String refer,
                                                 @Header("Cookie")String cookie);

    @GET("welfare_android")
    @Headers({"Accept-Encoding: gzip,deflate",
            "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
            "User-Agent: Mozilla/5.0 (Linux; Android 5.0.2; MI 2 Build/LRX22G) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/37.0.0.0 Mobile Safari/537.36",
            "X-Requested-With: com.hoyotech.lucky_draw"})
    Call<ResponseBody> getWelfareCookie(@Query("phone")String phone,
                                        @Query("sessionId")String sessionId);

    @GET("redEnvelopes/grabEnvelopes")
    Observable<GrabInfoEntity> grabWelfare(@Header("Referer")String refer,
                                           @Header("Cookie")String cookie);

    @POST("redEnvelopes/grabEnvelopesRecord")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Observable<WelfareRecordEntity> getWelfareRecord(@Header("Referer")String refer,
                                                     @Header("Cookie")String cookie,
                                                     @Body PhonePostBody body);
}
