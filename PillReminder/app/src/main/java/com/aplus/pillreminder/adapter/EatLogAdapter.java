package com.aplus.pillreminder.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aplus.pillreminder.R;
import com.aplus.pillreminder.model.EatLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EatLogAdapter extends RecyclerView.Adapter<EatLogAdapter.ViewHolder> {
    private Context context;
    private List<EatLog> data;

    public EatLogAdapter(Context context) {
        this.context = context;
        this.data = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_eat_log_list, parent, false);

        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        EatLog eatLog = data.get(position);
        holder.tvTime.setText(eatLog.getTime());
        holder.tvName.setText(eatLog.getPillName());
        holder.tvDose.setText(String.valueOf(eatLog.getDose()));

        if (eatLog.isTaken()) {
            setTextColor(holder, context.getResources().getColor(R.color.colorGreen));
        } else {
            setTextColor(holder, context.getResources().getColor(R.color.colorRed));
        }
    }

    private void setTextColor(ViewHolder holder, int color) {
        holder.tvTime.setTextColor(color);
        holder.tvName.setTextColor(color);
        holder.tvDose.setTextColor(color);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<EatLog> data) {
        this.data = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTime) TextView tvTime;
        @BindView(R.id.tvName) TextView tvName;
        @BindView(R.id.tvDose) TextView tvDose;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
