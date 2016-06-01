package com.lh.flux.view.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lh.flux.R;
import com.lh.flux.model.entity.WelfareRecordEntity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WelfareRecyclerAdapter extends RecyclerView.Adapter {
    private ArrayList<WelfareRecordEntity.Data> data;

    public ArrayList<WelfareRecordEntity.Data> getData() {
        return data;
    }

    public void setData(ArrayList<WelfareRecordEntity.Data> data) {
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup p1, int p2) {
        View view = LayoutInflater.from(p1.getContext()).inflate(R.layout.welfare_record_item, p1, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder p1, int p2) {
        Holder holder = (Holder) p1;
        holder.tvWelfareDes.setText(data.get(p2).getPrize().getDescription());
        holder.tvWelfareTime.setText(data.get(p2).getAddtime());
        holder.tvWelfareStatus.setText(data.get(p2).getStatus() == 0 ? "未充值" : "已充值");
        holder.tvWelfareStatus.setTextColor(data.get(p2).getStatus() == 0 ? Color.RED : Color.GREEN);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_welfare_des)
        TextView tvWelfareDes;
        @BindView(R.id.tv_welfare_time)
        TextView tvWelfareTime;
        @BindView(R.id.tv_welfare_status)
        TextView tvWelfareStatus;

        public Holder(View view) {
            super(view);
            ButterKnife.bind(this, itemView);
        }
    }

}
