package com.oleg.smarthomedashboard.fragment;

import static com.oleg.smarthomedashboard.MainActivity.getConfigurationHelperList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oleg.smarthomedashboard.MainActivity;
import com.oleg.smarthomedashboard.R;
import com.oleg.smarthomedashboard.adapter.ScenarioAddNewGridAdapter;
import com.oleg.smarthomedashboard.adapter.ScenarioAddNewSourcesAdapter;
import com.oleg.smarthomedashboard.helper.ScenarioAddNewHelper;
import com.oleg.smarthomedashboard.widget.ScenarioAddNewButtonClass;

import java.util.ArrayList;
import java.util.List;

public class ScenarioAddNew extends Fragment {
    private final List<ScenarioAddNewHelper> scenarioAddNewHelpers = new ArrayList<>();
    private final ArrayList<Integer> drawable = new ArrayList<>();
    private final ArrayList<Integer> place = new ArrayList<>();
    private final ArrayList<Integer> text = new ArrayList<>();
    private final ArrayList<String> ids = new ArrayList<>();
    private final ArrayList<Integer> buttonTypes = new ArrayList<>();
    private final ArrayList<Boolean> isOn = new ArrayList<>();
    private ScenarioAddNewSourcesAdapter scenarioSourcesAdapter;
    private ScenarioAddNewGridAdapter actionsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scenario_add_new, container, false);
    }

    private final View.OnClickListener listenerActions = new View.OnClickListener() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onClick(View view) {
            drawable.remove((int) view.getTag());
            place.remove((int) view.getTag());
            text.remove((int) view.getTag());
            isOn.remove((int) view.getTag());
            ids.remove((int) view.getTag());
            buttonTypes.remove((int) view.getTag());
            actionsAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView actions = view.findViewById(R.id.scenario_new_actions_grid);
        actions.setLayoutManager(new GridLayoutManager(MainActivity.getContext(), 5));
        actions.setHasFixedSize(true);
        actionsAdapter = new ScenarioAddNewGridAdapter(drawable, place, text, isOn, ids, buttonTypes);
        actionsAdapter.setListener(listenerActions);
        actions.setAdapter(actionsAdapter);
        RecyclerView sources = view.findViewById(R.id.scenario_new_sources);
        scenarioSourcesAdapter = new ScenarioAddNewSourcesAdapter(scenarioAddNewHelpers);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(requireActivity().getApplicationContext());
        sources.setLayoutManager(mLayoutManager);
        sources.setItemAnimator(new DefaultItemAnimator());
        sources.setAdapter(scenarioSourcesAdapter);

        Button saveButton = view.findViewById(R.id.scenario_add_new_save);
        EditText txt = view.findViewById(R.id.scenario_new_name);
        saveButton.setOnClickListener(view1 -> {
            StringBuilder s = new StringBuilder();
            for (int i = 0; i < ids.size(); i++) {
                s.append(ids.get(i)).append(":").append(isOn.get(i) ? "1" : "0").append("_");
            }
            txt.setText(s);
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        PopulateSources();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void PopulateSources() {
        for (int i = 0; i < getConfigurationHelperList().size(); i++) {
            ScenarioAddNewButtonClass button_1 = new ScenarioAddNewButtonClass(
                    getConfigurationHelperList().get(i).getButton1().getButtonID(),
                    getConfigurationHelperList().get(i).getButton1().getButtonType(),
                    getConfigurationHelperList().get(i).getButton1().getButtonDrawableID(),
                    getConfigurationHelperList().get(i).getTitleTextID(),
                    getConfigurationHelperList().get(i).getButton1().getButtonNote());
            listenerSources(button_1.getButton(false), button_1);
            ScenarioAddNewButtonClass button_2 = new ScenarioAddNewButtonClass(
                    getConfigurationHelperList().get(i).getButton2().getButtonID(),
                    getConfigurationHelperList().get(i).getButton2().getButtonType(),
                    getConfigurationHelperList().get(i).getButton2().getButtonDrawableID(),
                    getConfigurationHelperList().get(i).getTitleTextID(),
                    getConfigurationHelperList().get(i).getButton2().getButtonNote());
            listenerSources(button_2.getButton(false), button_2);
            ScenarioAddNewButtonClass button_3 = new ScenarioAddNewButtonClass(
                    getConfigurationHelperList().get(i).getButton3().getButtonID(),
                    getConfigurationHelperList().get(i).getButton3().getButtonType(),
                    getConfigurationHelperList().get(i).getButton3().getButtonDrawableID(),
                    getConfigurationHelperList().get(i).getTitleTextID(),
                    getConfigurationHelperList().get(i).getButton3().getButtonNote());
            listenerSources(button_3.getButton(false), button_3);
            ScenarioAddNewButtonClass button_4 = new ScenarioAddNewButtonClass(
                    getConfigurationHelperList().get(i).getButton4().getButtonID(),
                    getConfigurationHelperList().get(i).getButton4().getButtonType(),
                    getConfigurationHelperList().get(i).getButton4().getButtonDrawableID(),
                    getConfigurationHelperList().get(i).getTitleTextID(),
                    getConfigurationHelperList().get(i).getButton4().getButtonNote());
            listenerSources(button_4.getButton(false), button_4);
            ScenarioAddNewHelper scenarioAddNewHelper = new ScenarioAddNewHelper(getConfigurationHelperList().get(i).getTitleTextID(), button_1, button_2, button_3, button_4);
            scenarioAddNewHelpers.add(scenarioAddNewHelper);
        }
        scenarioSourcesAdapter.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void listenerSources(View view, ScenarioAddNewButtonClass conf) {
        if (view != null)
            conf.SetOnItemClickListener(view1 -> new AlertDialog.Builder(getActivity())
                    .setTitle("Действие")
                    .setPositiveButton("Включить", (dialogInterface, i) -> {
                        drawable.add((Integer) view.getTag());
                        place.add(conf.getButtonPlaceId());
                        text.add(conf.getButtonTextId());
                        isOn.add(true);
                        ids.add(conf.getButtonID());
                        buttonTypes.add(conf.getButtonType());
                        actionsAdapter.notifyDataSetChanged();
                    })
                    .setNegativeButton("Выключить", (dialogInterface, i) -> {
                        drawable.add((Integer) view.getTag());
                        place.add(conf.getButtonPlaceId());
                        text.add(conf.getButtonTextId());
                        isOn.add(false);
                        ids.add(conf.getButtonID());
                        buttonTypes.add(conf.getButtonType());
                        actionsAdapter.notifyDataSetChanged();
                    })
                    .show());
    }
}