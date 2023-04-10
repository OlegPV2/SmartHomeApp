package com.oleg.smarthomedashboard.fragments.elements;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oleg.smarthomedashboard.R;

import java.util.List;

public class MainDashboardAdapter extends RecyclerView.Adapter<MainDashboardAdapter.MyViewHolder> {
    private final List<DashboardInfo> dashboardInfoList;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView head_image;
        public TextView head_text_field, temperature_field, humidity_field, humidity_mark_field;
        public LinearLayout climate, buttons_field;

        public MyViewHolder(View view) {
            super(view);
            head_image = view.findViewById(R.id.head_image);
            head_text_field = view.findViewById(R.id.head_text);
            temperature_field = view.findViewById(R.id.temp);
            humidity_field = view.findViewById(R.id.humidity);
            humidity_mark_field = view.findViewById(R.id.humidity_mark);
            climate = view.findViewById(R.id.climate);
            buttons_field = view.findViewById(R.id.buttons_field);
        }
    }

    public MainDashboardAdapter(List<DashboardInfo> dashboardInfoList) {
        this.dashboardInfoList = dashboardInfoList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_main_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DashboardInfo dashboardInfo = dashboardInfoList.get(position);
        if (dashboardInfo.getHeadIcon() != R.integer.nothing)
            holder.head_image.setImageResource(dashboardInfo.getHeadIcon());
        else
            holder.head_image.setImageResource(0);
        holder.head_text_field.setText(dashboardInfo.getHeadText());
        if (!dashboardInfo.getShowHumidity() && !dashboardInfo.getShowTemperature()) {
            holder.climate.setVisibility(View.GONE);
        } else if (!dashboardInfo.getShowHumidity()) {
            holder.humidity_field.setVisibility(View.GONE);
            holder.humidity_mark_field.setVisibility(View.GONE);
            holder.temperature_field.setId(dashboardInfo.getTextIDTemperature());
        } else {
            holder.temperature_field.setId(dashboardInfo.getTextIDHumidity());
            holder.temperature_field.setId(dashboardInfo.getTextIDTemperature());
        }
        if (dashboardInfo.getButton_1() != null) holder.buttons_field.addView(dashboardInfo.getButton_1());
        if (dashboardInfo.getButton_2() != null) holder.buttons_field.addView(dashboardInfo.getButton_2());
        if (dashboardInfo.getButton_3() != null) holder.buttons_field.addView(dashboardInfo.getButton_3());
        if (dashboardInfo.getButton_4() != null) holder.buttons_field.addView(dashboardInfo.getButton_4());
    }

    @Override
    public int getItemCount() {
        return dashboardInfoList.size();
    }
}
