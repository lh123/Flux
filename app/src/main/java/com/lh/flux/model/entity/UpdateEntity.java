package com.lh.flux.model.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuhui on 2016/5/22.
 * updateEntity
 */
@SuppressWarnings("unused")
public class UpdateEntity {
    //    {
//        "apkFile": {
//        "__type": "File",
//                "cdn": "upyun",
//                "filename": "app-release.apk",
//                "url": "http://bmob-cdn-1663.b0.upaiyun.com/2016/05/22/8f9655ec4017859080873ce76ccae27d.apk"
//    },
//        "appName": "Flux",
//            "createdAt": "2016-05-22 12:41:44",
//            "objectId": "2QxJeeey",
//            "updatedAt": "2016-05-22 14:57:17",
//            "url": "http://bmob-cdn-1663.b0.upaiyun.com/2016/05/22/8f9655ec4017859080873ce76ccae27d.apk",
//            "visionCode": 5
//    }
    private String appName;
    private int visionCode;
    private ApkFile apkFile;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public int getVisionCode() {
        return visionCode;
    }

    public void setVisionCode(int visionCode) {
        this.visionCode = visionCode;
    }

    public ApkFile getApkFile() {
        return apkFile;
    }

    public void setApkFile(ApkFile apkFile) {
        this.apkFile = apkFile;
    }

    public static class ApkFile {
        @SerializedName("__type")
        private String type;
        private String cdn;
        private String filename;
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCdn() {
            return cdn;
        }

        public void setCdn(String cdn) {
            this.cdn = cdn;
        }

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }
    }
}
