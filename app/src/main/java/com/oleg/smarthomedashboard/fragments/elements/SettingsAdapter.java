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

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.MyViewHolder> {
    private final List<SettingsInfo> settingsInfoList;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView head_text_field;
        public LinearLayout container;

        public MyViewHolder(View view) {
            super(view);
            head_text_field = view.findViewById(R.id.settings_card_head_text);
            container = view.findViewById(R.id.settings_card_container);
        }
    }

    public SettingsAdapter(List<SettingsInfo> settingsInfoList) {
        this.settingsInfoList = settingsInfoList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_settings_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SettingsInfo settingsInfo = settingsInfoList.get(position);
        holder.head_text_field.setText(settingsInfo.getHeadText());
        for (int i = 0; i < settingsInfo.getNumberOfFields(); i++)
            holder.container.addView(settingsInfo.getFields(i));
    }

    @Override
    public int getItemCount() {
        return settingsInfoList.size();
    }
}
