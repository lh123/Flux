package com.lh.flux.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.lh.flux.FluxApp;
import com.lh.flux.FluxAppComponent;

/**
 * Created by liuhui on 2016/5/12.
 * BaseActivity
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpComponent();
    }

    protected abstract void setUpComponent();

    protected FluxAppComponent getAppComponent() {
        return FluxApp.getApp().getAppComponent();
    }
}
