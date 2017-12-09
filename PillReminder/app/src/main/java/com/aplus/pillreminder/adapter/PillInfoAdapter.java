package com.aplus.pillreminder.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aplus.pillreminder.R;
import com.aplus.pillreminder.model.Pill;
import com.aplus.pillreminder.model.PillWithRemindTime;

import java.text.NumberFormat;
import java.util.List;

public class PillInfoAdapter extends ArrayAdapter<PillWithRemindTime> {

    private Context context;
    private int layoutResourceId;
    private List<PillWithRemindTime> data;

    public PillInfoAdapter(@NonNull Context context, int layoutResourceId, @NonNull List<PillWithRemindTime> data) {
        super(context, layoutResourceId, data);

        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ViewHolder();
            holder.imgPill = row.findViewById(R.id.imgPill);
            holder.tvName = row.findViewById(R.id.tvName);
            holder.tvQuantity = row.findViewById(R.id.tvQuantity);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        Pill pill = data.get(position).getPill();
        holder.imgPill.setColorFilter(pill.getColor());
        holder.tvName.setText(pill.getName());
        holder.tvQuantity.setText(NumberFormat.getInstance().format(pill.getQuantity()));

        return row;
    }

    public class ViewHolder {
        ImageView imgPill;
        TextView tvName;
        TextView tvQuantity;
    }
}
