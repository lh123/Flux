package com.lh.flux;

import android.app.Application;
import android.content.Context;

import com.lh.flux.domain.FluxUserManager;
import com.lh.flux.domain.UpdateManager;
import com.lh.flux.model.entity.User;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by liuhui on 2016/5/12.
 * FluxAppModule
 */
@Module
public class FluxAppModule {
    private Application application;

    public FluxAppModule(Application application) {
        this.application = application;
    }

    @Singleton
    @Provides
    public User provideUser(FluxUserManager userManager) {
        return userManager.getUser();
    }

    @Singleton
    @Provides
    public FluxUserManager provideUserManager() {
        return new FluxUserManager(application);
    }

    @Singleton
    @Provides
    Context provideContext() {
        return application;
    }

    @Singleton
    @Provides
    RefWatcher provideRefWatcher() {
        return LeakCanary.install(application);
    }

    @Singleton
    @Provides
    UpdateManager provideUpdateManager() {
        return new UpdateManager(application);
    }
}
