package com.oleg.smarthomedashboard.fragment;

import static com.oleg.smarthomedashboard.MainActivity.configurationHelperList;

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
    ScenarioAddNewSourcesAdapter scenarioSourcesAdapter;
    RecyclerView sources;
    RecyclerView actions;
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
//            actions.setAdapter(gridAdapter);
        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        actions = view.findViewById(R.id.scenario_new_actions_grid);
        actions.setLayoutManager(new GridLayoutManager(MainActivity.getContext(), 5));
        actions.setHasFixedSize(true);
        gridAdapter = new ScenarioAddNewGridAdapter(drawable, place, text, isOn, ids, buttonTypes);
        gridAdapter.setListener(listenerActions);
        actions.setAdapter(gridAdapter);
        sources = view.findViewById(R.id.scenario_new_sources);
        scenarioSourcesAdapter = new ScenarioAddNewSourcesAdapter(scenarioAddNewHelpers);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(requireActivity().getApplicationContext());
        sources.setLayoutManager(mLayoutManager);
        sources.setItemAnimator(new DefaultItemAnimator());
        sources.setAdapter(scenarioSourcesAdapter);

        Button saveButton = view.findViewById(R.id.scenario_add_new_save);
        EditText txt = view.findViewById(R.id.scenario_new_name);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder s = new StringBuilder();
                for (int i = 0; i < ids.size(); i++) {
                    s.append(getResources().getResourceEntryName(ids.get(i))).append(":").append(isOn.get(i) ? "1" : "0").append("_");
                }
                txt.setText(s);
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        PopulateSources();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void PopulateSources() {
        for (int i = 0; i < configurationHelperList.size(); i++) {
            ScenarioAddNewButtonClass button_1 = new ScenarioAddNewButtonClass(
                    configurationHelperList.get(i).getButton1().getButtonID(),
                    configurationHelperList.get(i).getButton1().getButtonType(),
                    configurationHelperList.get(i).getButton1().getButtonDrawableID(),
                    configurationHelperList.get(i).getTitleTextID(),
                    configurationHelperList.get(i).getButton1().getButtonNote());
            listenerSources(button_1.getButton(false), button_1);
            ScenarioAddNewButtonClass button_2 = new ScenarioAddNewButtonClass(
                    configurationHelperList.get(i).getButton2().getButtonID(),
                    configurationHelperList.get(i).getButton2().getButtonType(),
                    configurationHelperList.get(i).getButton2().getButtonDrawableID(),
                    configurationHelperList.get(i).getTitleTextID(),
                    configurationHelperList.get(i).getButton2().getButtonNote());
            listenerSources(button_2.getButton(false), button_2);
            ScenarioAddNewButtonClass button_3 = new ScenarioAddNewButtonClass(
                    configurationHelperList.get(i).getButton3().getButtonID(),
                    configurationHelperList.get(i).getButton3().getButtonType(),
                    configurationHelperList.get(i).getButton3().getButtonDrawableID(),
                    configurationHelperList.get(i).getTitleTextID(),
                    configurationHelperList.get(i).getButton3().getButtonNote());
            listenerSources(button_3.getButton(false), button_3);
            ScenarioAddNewButtonClass button_4 = new ScenarioAddNewButtonClass(
                    configurationHelperList.get(i).getButton4().getButtonID(),
                    configurationHelperList.get(i).getButton4().getButtonType(),
                    configurationHelperList.get(i).getButton4().getButtonDrawableID(),
                    configurationHelperList.get(i).getTitleTextID(),
                    configurationHelperList.get(i).getButton4().getButtonNote());
            listenerSources(button_4.getButton(false), button_4);
            ScenarioAddNewHelper scenarioAddNewHelper = new ScenarioAddNewHelper(configurationHelperList.get(i).getTitleTextID(), button_1, button_2, button_3, button_4);
            scenarioAddNewHelpers.add(scenarioAddNewHelper);
        }
        scenarioSourcesAdapter.notifyDataSetChanged();
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
//                        actions.setAdapter(gridAdapter);
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
//                        actions.setAdapter(gridAdapter);
//                    AppDialog.INSTANCE.dismissDialogFragment(getSupportFragmentManager());
                    })
                    .show());
    }
}