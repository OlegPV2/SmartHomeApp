package com.oleg.smarthomedashboard.fragments;

import static com.oleg.smarthomedashboard.MainActivity.configurationInfoList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oleg.smarthomedashboard.R;
import com.oleg.smarthomedashboard.fragments.elements.ScenarioAddNewButtonClass;
import com.oleg.smarthomedashboard.fragments.elements.ScenarioAddNewGridAdapter;
import com.oleg.smarthomedashboard.fragments.elements.ScenarioAddNewInfo;
import com.oleg.smarthomedashboard.fragments.elements.ScenarioAddNewSourcesAdapter;

import java.util.ArrayList;
import java.util.List;

public class ScenarioAddNew extends Fragment {
    private final List<ScenarioAddNewInfo> scenarioAddNewInfos = new ArrayList<>();
    ScenarioAddNewSourcesAdapter scenarioSourcesAdapter;
    RecyclerView sources;
    GridView actions;
    ScenarioAddNewGridAdapter gridAdapter;
    ArrayList<Integer> drawable = new ArrayList<>();
    ArrayList<Integer> place = new ArrayList<>();
    ArrayList<Integer> text = new ArrayList<>();
    ArrayList<Integer> ids = new ArrayList<>();
    ArrayList<Integer> buttonTypes = new ArrayList<>();
    ArrayList<Boolean> isOn = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scenario_add_new, container, false);
    }

    View.OnClickListener listenerActions = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            drawable.remove((int) view.getTag());
            place.remove((int) view.getTag());
            text.remove((int) view.getTag());
            isOn.remove((int) view.getTag());
            ids.remove((int) view.getTag());
            buttonTypes.remove((int) view.getTag());
            gridAdapter.notifyDataSetChanged();
            actions.setAdapter(gridAdapter);
        }
    };

    @SuppressLint("NotifyDataSetChanged")
    private void PopulateSources() {
        for (int i = 0; i < configurationInfoList.size(); i++) {
            ScenarioAddNewButtonClass button_1 = new ScenarioAddNewButtonClass(
                    configurationInfoList.get(i).getButton1().getButtonID(),
                    configurationInfoList.get(i).getButton1().getButtonType(),
                    configurationInfoList.get(i).getButton1().getButtonDrawableID(),
                    configurationInfoList.get(i).getTitleTextID(),
                    configurationInfoList.get(i).getButton1().getButtonNote());
            listenerSources(button_1.getButton(false), button_1);
            ScenarioAddNewButtonClass button_2 = new ScenarioAddNewButtonClass(
                    configurationInfoList.get(i).getButton2().getButtonID(),
                    configurationInfoList.get(i).getButton2().getButtonType(),
                    configurationInfoList.get(i).getButton2().getButtonDrawableID(),
                    configurationInfoList.get(i).getTitleTextID(),
                    configurationInfoList.get(i).getButton2().getButtonNote());
            listenerSources(button_2.getButton(false), button_2);
            ScenarioAddNewButtonClass button_3 = new ScenarioAddNewButtonClass(
                    configurationInfoList.get(i).getButton3().getButtonID(),
                    configurationInfoList.get(i).getButton3().getButtonType(),
                    configurationInfoList.get(i).getButton3().getButtonDrawableID(),
                    configurationInfoList.get(i).getTitleTextID(),
                    configurationInfoList.get(i).getButton3().getButtonNote());
            listenerSources(button_3.getButton(false), button_3);
            ScenarioAddNewButtonClass button_4 = new ScenarioAddNewButtonClass(
                    configurationInfoList.get(i).getButton4().getButtonID(),
                    configurationInfoList.get(i).getButton4().getButtonType(),
                    configurationInfoList.get(i).getButton4().getButtonDrawableID(),
                    configurationInfoList.get(i).getTitleTextID(),
                    configurationInfoList.get(i).getButton4().getButtonNote());
            listenerSources(button_4.getButton(false), button_4);
            ScenarioAddNewInfo scenarioAddNewInfo = new ScenarioAddNewInfo(configurationInfoList.get(i).getTitleTextID(), button_1, button_2, button_3, button_4);
            scenarioAddNewInfos.add(scenarioAddNewInfo);
        }
        scenarioSourcesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        actions = view.findViewById(R.id.scenario_new_actions_grid);
        gridAdapter = new ScenarioAddNewGridAdapter(drawable, place, text, isOn, ids, buttonTypes);
        gridAdapter.setListener(listenerActions);
        actions.setAdapter(gridAdapter);
        sources = view.findViewById(R.id.scenario_new_sources);
        scenarioSourcesAdapter = new ScenarioAddNewSourcesAdapter(scenarioAddNewInfos);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(requireActivity().getApplicationContext());
        sources.setLayoutManager(mLayoutManager);
        sources.setItemAnimator(new DefaultItemAnimator());
        sources.setAdapter(scenarioSourcesAdapter);

        PopulateSources();
    }

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
                        gridAdapter.notifyDataSetChanged();
                        actions.setAdapter(gridAdapter);
//                    AppDialog.INSTANCE.dismissDialogFragment(getSupportFragmentManager());
                    })
                    .setNegativeButton("Выключить", (dialogInterface, i) -> {
                        drawable.add((Integer) view.getTag());
                        place.add(conf.getButtonPlaceId());
                        text.add(conf.getButtonTextId());
                        isOn.add(false);
                        ids.add(conf.getButtonID());
                        buttonTypes.add(conf.getButtonType());
                        gridAdapter.notifyDataSetChanged();
                        actions.setAdapter(gridAdapter);
//                    AppDialog.INSTANCE.dismissDialogFragment(getSupportFragmentManager());
                    })
                    .show());
    }
}