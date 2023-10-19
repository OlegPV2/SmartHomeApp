package com.oleg.smarthomedashboard.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oleg.smarthomedashboard.R;
import com.oleg.smarthomedashboard.helper.SettingsHelper;

import java.util.List;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.MyViewHolder> {
    private final List<SettingsHelper> settingsHelperList;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public final TextView head_text_field;
        public final LinearLayout container;

        public MyViewHolder(View view) {
            super(view);
            head_text_field = view.findViewById(R.id.settings_card_head_text);
            container = view.findViewById(R.id.settings_card_container);
        }
    }

    public SettingsAdapter(List<SettingsHelper> settingsHelperList) {
        this.settingsHelperList = settingsHelperList;
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
        SettingsHelper settingsHelper = settingsHelperList.get(position);
        holder.head_text_field.setText(settingsHelper.getHeadText());
        for (int i = 0; i < settingsHelper.getNumberOfFields(); i++)
            holder.container.addView(settingsHelper.getFields(i));
    }

    @Override
    public int getItemCount() {
        return settingsHelperList.size();
    }
}
