package com.oleg.smarthomedashboard.fragments.elements;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oleg.smarthomedashboard.R;

import java.util.List;

public class ScenarioAddNewSourcesAdapter extends RecyclerView.Adapter<ScenarioAddNewSourcesAdapter.ViewHolder> {
    private final List<ScenarioAddNewInfo> scenarioAddNewInfos;

    public ScenarioAddNewSourcesAdapter(List<ScenarioAddNewInfo> scenarioAddNewInfos) {
        this.scenarioAddNewInfos = scenarioAddNewInfos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_scenario_add_new_card, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ScenarioAddNewInfo scenarioAddNewInfo = scenarioAddNewInfos.get(position);
        holder.head.setText(scenarioAddNewInfo.getHeadText());
        if (scenarioAddNewInfo.getButton_1() != null)
            holder.buttonField.addView(scenarioAddNewInfo.getButton_1());
        if (scenarioAddNewInfo.getButton_2() != null)
            holder.buttonField.addView(scenarioAddNewInfo.getButton_2());
        if (scenarioAddNewInfo.getButton_3() != null)
            holder.buttonField.addView(scenarioAddNewInfo.getButton_3());
        if (scenarioAddNewInfo.getButton_4() != null)
            holder.buttonField.addView(scenarioAddNewInfo.getButton_4());
    }

    @Override
    public int getItemCount() {
        return scenarioAddNewInfos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView head;
        LinearLayout buttonField;

        ViewHolder(View itemView) {
            super(itemView);
            head = itemView.findViewById(R.id.scenario_sources_head_text);
            buttonField = itemView.findViewById(R.id.buttons_field);
        }
    }
}
