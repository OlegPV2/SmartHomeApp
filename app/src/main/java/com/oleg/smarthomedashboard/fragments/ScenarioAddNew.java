package com.oleg.smarthomedashboard.fragments;

import static com.oleg.smarthomedashboard.MainActivity.configurationInfoList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oleg.smarthomedashboard.R;
import com.oleg.smarthomedashboard.fragments.elements.ScenarioAddNewButtonClass;
import com.oleg.smarthomedashboard.fragments.elements.ScenarioAddNewInfo;
import com.oleg.smarthomedashboard.fragments.elements.ScenarioAddNewSourcesAdapter;

import java.util.ArrayList;
import java.util.List;

public class ScenarioAddNew extends Fragment {
    private final List<ScenarioAddNewInfo> scenarioAddNewInfos = new ArrayList<>();
    private final View.OnClickListener listenerActions = view -> view.setVisibility(View.GONE);
    ScenarioAddNewSourcesAdapter scenarioSourcesAdapter;
    RecyclerView sources;
    LinearLayout actions;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scenario_add_new, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sources = view.findViewById(R.id.scenario_new_sources);
        actions = view.findViewById(R.id.scenario_new_actions);
        scenarioSourcesAdapter = new ScenarioAddNewSourcesAdapter(scenarioAddNewInfos);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(requireActivity().getApplicationContext());
        sources.setLayoutManager(mLayoutManager);
        sources.setItemAnimator(new DefaultItemAnimator());
        sources.setAdapter(scenarioSourcesAdapter);

        PopulateSources();
    }

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

    private void listenerSources(View view, ScenarioAddNewButtonClass conf) {
        if (view != null)
            conf.SetOnItemClickListener(view1 -> new AlertDialog.Builder(getActivity())
                    .setTitle("Действие")
                    .setPositiveButton("Включить", (dialogInterface, i) -> {
                        View button = conf.getButton(true);
                        button.setOnClickListener(listenerActions);
                        button.setBackground(
                                ResourcesCompat.getDrawable(
                                        view1.getResources(),
                                        getBckColor(conf.getButtonType()),
                                        view1.getContext().getTheme()
                                )
                        );
                        actions.addView(button);
//                    AppDialog.INSTANCE.dismissDialogFragment(getSupportFragmentManager());
                    })
                    .setNegativeButton("Выключить", (dialogInterface, i) -> {
                        View button = conf.getButton(true);
                        button.setOnClickListener(listenerActions);
                        actions.addView(button);
//                    AppDialog.INSTANCE.dismissDialogFragment(getSupportFragmentManager());
                    })
                    .show());
    }

    private int getBckColor(int buttonType) {
        switch (buttonType) {
            case 1:
                return R.drawable.rounded_corners_yellow;
            case 4:
                return R.drawable.rounded_corners_amber;
            case 2:
                return R.drawable.rounded_corners_green;
            case 3:
                return R.drawable.rounded_corners_blue;
        }
        return R.color.error;
    }
}