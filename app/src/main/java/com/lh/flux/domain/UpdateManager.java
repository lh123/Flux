package com.lh.flux.domain;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.lh.flux.FluxApp;
import com.lh.flux.model.api.UpdateApi;
import com.lh.flux.model.entity.UpdateEntity;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liuhui on 2016/5/22.
 * updateUtil
 */
public class UpdateManager {
    private UpdateApi updateApi;
    private int currentCode = -1;
    private Context context;

    public UpdateManager(Context context) {
        this.context = context;
        updateApi = FluxApp.getApp().getAppComponent().getRetrofit().create(UpdateApi.class);
        try {
            currentCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void checkUpdate(final UpdateCallBack callBack) {
        updateApi.getUpdateInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UpdateEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        callBack.onFail();
                    }

                    @Override
                    public void onNext(UpdateEntity updateEntity) {
                        callBack.onSuccess(updateEntity, updateEntity.getVisionCode() > currentCode);
                    }
                });
    }

    public void downloadApk(UpdateEntity.ApkFile file) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(file.getUrl()));
        request.setTitle(file.getFilename());
        request.setDescription("软件更新");
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        request.allowScanningByMediaScanner();
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, file.getFilename());
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        long id = downloadManager.enqueue(request);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putLong("downloadId", id).apply();
        context.registerReceiver(new InstallReceiver(), new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    public interface UpdateCallBack {
        void onSuccess(UpdateEntity updateEntity, boolean isNeedUpdate);

        void onFail();
    }

    public static class InstallReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                context.unregisterReceiver(this);
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                long savedID = sp.getLong("downloadId", -1);
                long downloadApkId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (savedID == downloadApkId) {
                    DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                    Intent install = new Intent(Intent.ACTION_VIEW);
                    Uri downUri = downloadManager.getUriForDownloadedFile(downloadApkId);
                    if (downUri != null) {
                        install.setDataAndType(downUri, "application/vnd.android.package-archive");
                        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(install);
                    } else {
                        Toast.makeText(context, "下载失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
}