package com.oleg.smarthomedashboard.fragments;

import static com.oleg.smarthomedashboard.MainActivity.configurationInfoList;
import static com.oleg.smarthomedashboard.MainActivity.metersInfoList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oleg.smarthomedashboard.R;
import com.oleg.smarthomedashboard.fragments.elements.SettingsAdapter;
import com.oleg.smarthomedashboard.fragments.elements.SettingsInfo;

import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends Fragment {
    private final List<SettingsInfo> settingsInfoList = new ArrayList<>();
    private SettingsAdapter settingsAdapter;
    RecyclerView container;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        container = view.findViewById(R.id.settings_fragment_container);
        settingsAdapter = new SettingsAdapter(settingsInfoList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(requireActivity().getApplicationContext());
        container.setLayoutManager(mLayoutManager);
        container.setItemAnimator(new DefaultItemAnimator());
        container.setAdapter(settingsAdapter);

        PopulateView();
//        CreateWebSocketClient.sendMessage("Settings");
    }

    @SuppressLint("NotifyDataSetChanged")
    private void PopulateView() {
        for (int i = 0; i < configurationInfoList.size(); i++) {
            SettingsInfo settingsInfo = new SettingsInfo(configurationInfoList.get(i));
            settingsInfoList.add(settingsInfo);
        }
        SettingsInfo metersInfo = new SettingsInfo(metersInfoList);
        settingsInfoList.add(metersInfo);
        settingsAdapter.notifyDataSetChanged();
    }
}