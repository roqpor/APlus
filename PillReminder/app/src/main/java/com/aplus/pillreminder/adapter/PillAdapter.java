package com.aplus.pillreminder.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aplus.pillreminder.R;
import com.aplus.pillreminder.model.Pill;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PillAdapter extends RecyclerView.Adapter<PillAdapter.ViewHolder> {
    private List<Pill> pills;

    public PillAdapter(List<Pill> pills) {
        this.pills = pills;
        pills.add(new Pill());
        pills.add(new Pill());
        pills.add(new Pill());
        pills.add(new Pill());
        pills.add(new Pill());
        pills.add(new Pill());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_pill_grid, null, false);
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Pill pill = pills.get(position);
        holder.imageView.setColorFilter(Color.parseColor("#00CECB"));
    }

    @Override
    public int getItemCount() {
        return pills.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageView)
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
