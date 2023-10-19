package com.oleg.smarthomedashboard.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oleg.smarthomedashboard.R;
import com.oleg.smarthomedashboard.helper.ScenarioAddNewHelper;

import java.util.List;

public class ScenarioAddNewSourcesAdapter extends RecyclerView.Adapter<ScenarioAddNewSourcesAdapter.ViewHolder> {
    private final List<ScenarioAddNewHelper> scenarioAddNewHelpers;

    public ScenarioAddNewSourcesAdapter(List<ScenarioAddNewHelper> scenarioAddNewHelpers) {
        this.scenarioAddNewHelpers = scenarioAddNewHelpers;
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
        ScenarioAddNewHelper scenarioAddNewHelper = scenarioAddNewHelpers.get(position);
        holder.head.setText(scenarioAddNewHelper.getHeadText());
        if (scenarioAddNewHelper.getButton_1() != null)
            holder.buttonField.addView(scenarioAddNewHelper.getButton_1());
        if (scenarioAddNewHelper.getButton_2() != null)
            holder.buttonField.addView(scenarioAddNewHelper.getButton_2());
        if (scenarioAddNewHelper.getButton_3() != null)
            holder.buttonField.addView(scenarioAddNewHelper.getButton_3());
        if (scenarioAddNewHelper.getButton_4() != null)
            holder.buttonField.addView(scenarioAddNewHelper.getButton_4());
    }

    @Override
    public int getItemCount() {
        return scenarioAddNewHelpers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView head;
        public final LinearLayout buttonField;

        ViewHolder(View itemView) {
            super(itemView);
            head = itemView.findViewById(R.id.scenario_sources_head_text);
            buttonField = itemView.findViewById(R.id.buttons_field);
        }
    }
}
