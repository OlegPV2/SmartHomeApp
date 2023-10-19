package com.oleg.smarthomedashboard.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oleg.smarthomedashboard.R;
import com.oleg.smarthomedashboard.helper.DashboardHelper;

import java.util.List;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.MyViewHolder> {
    private final List<DashboardHelper> dashboardHelperList;

    public DashboardAdapter(List<DashboardHelper> dashboardHelperList) {
        this.dashboardHelperList = dashboardHelperList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public final ImageView head_image;
        public final TextView head_text_field, temperature_field, humidity_field, humidity_mark_field;
        public final LinearLayout climate, buttons_field;

        public MyViewHolder(View view) {
            super(view);
            head_image = view.findViewById(R.id.head_image);
            head_text_field = view.findViewById(R.id.scenario_sources_head_text);
            temperature_field = view.findViewById(R.id.temp);
            humidity_field = view.findViewById(R.id.humidity);
            humidity_mark_field = view.findViewById(R.id.humidity_mark);
            climate = view.findViewById(R.id.climate);
            buttons_field = view.findViewById(R.id.buttons_field);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_dashboard_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DashboardHelper dashboardHelper = dashboardHelperList.get(position);
        if (dashboardHelper.getHeadIcon() != R.integer.nothing)
            holder.head_image.setImageResource(dashboardHelper.getHeadIcon());
        else
            holder.head_image.setImageResource(0);
        holder.head_text_field.setText(dashboardHelper.getHeadText());
        if (dashboardHelper.getShowHumidity()) {
            holder.humidity_field.setId(dashboardHelper.getTextIDHumidity());
            holder.temperature_field.setId(dashboardHelper.getTextIDTemperature());
        } else if (dashboardHelper.getShowTemperature()) {
            holder.humidity_field.setVisibility(View.GONE);
            holder.humidity_mark_field.setVisibility(View.GONE);
            holder.temperature_field.setId(dashboardHelper.getTextIDTemperature());
        } else {
            holder.climate.setVisibility(View.GONE);
        }
        if (dashboardHelper.getButton_1() != null)
            holder.buttons_field.addView(dashboardHelper.getButton_1());
        if (dashboardHelper.getButton_2() != null)
            holder.buttons_field.addView(dashboardHelper.getButton_2());
        if (dashboardHelper.getButton_3() != null)
            holder.buttons_field.addView(dashboardHelper.getButton_3());
        if (dashboardHelper.getButton_4() != null)
            holder.buttons_field.addView(dashboardHelper.getButton_4());
    }

    @Override
    public int getItemCount() {
        return dashboardHelperList.size();
    }
}
