package com.lh.flux.view;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.lh.flux.R;
import com.lh.flux.domain.utils.ThemeUtil;
import com.lh.flux.model.entity.WelfareRecordEntity;
import com.lh.flux.mvp.presenter.WelfareRecordPresenter;
import com.lh.flux.mvp.view.IWelfareRecordActivity;
import com.lh.flux.view.adapter.WelfareRecyclerAdapter;
import com.lh.flux.view.component.DaggerWelfareRecordActivityComponent;
import com.lh.flux.view.module.WelfareRecordActivityModule;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WelfareRecordActivity extends BaseActivity implements IWelfareRecordActivity, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.welfare_record_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.welfare_record_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @Inject
    WelfareRecordPresenter mPresenter;
    private WelfareRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtil.getInstance().setTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welfare_record_aty);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        mAdapter = new WelfareRecyclerAdapter();
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        assert mRecyclerView != null;
        mRecyclerView.setLayoutManager(lm);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mPresenter = new WelfareRecordPresenter(this);
        mPresenter.onCreate();
        mPresenter.startRefreshWelfareRecord();
    }

    @Override
    protected void setUpComponent() {
        DaggerWelfareRecordActivityComponent.builder()
                .welfareRecordActivityModule(new WelfareRecordActivityModule(this))
                .fluxAppComponent(getAppComponent())
                .build()
                .inject(this);
    }

    @Override
    public void onRefresh() {
        mPresenter.startRefreshWelfareRecord();
    }

    @Override
    public void setData(ArrayList<WelfareRecordEntity.Data> data) {
        int preSize = mAdapter.getItemCount();
        mAdapter.setData(data);
        if (preSize < data.size()) {
            mAdapter.notifyItemRangeInserted(preSize, data.size() - preSize);
        } else if (preSize > data.size()) {
            mAdapter.notifyItemRangeRemoved(data.size(), preSize - data.size());
        } else {
            mAdapter.notifyItemRangeChanged(0, data.size());
        }
    }

    @Override
    public void setRefreshStatus(boolean status) {
        mSwipeRefreshLayout.setRefreshing(status);
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }
}
