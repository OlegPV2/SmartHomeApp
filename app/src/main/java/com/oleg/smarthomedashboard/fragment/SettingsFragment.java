package com.oleg.smarthomedashboard.fragment;

import static com.oleg.smarthomedashboard.MainActivity.configurationHelperList;
import static com.oleg.smarthomedashboard.MainActivity.settingsMetersHelperList;

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
import com.oleg.smarthomedashboard.adapter.SettingsAdapter;
import com.oleg.smarthomedashboard.helper.SettingsHelper;

import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends Fragment {
    private final List<SettingsHelper> settingsHelperList = new ArrayList<>();
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
        settingsAdapter = new SettingsAdapter(settingsHelperList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(requireActivity().getApplicationContext());
        container.setLayoutManager(mLayoutManager);
        container.setItemAnimator(new DefaultItemAnimator());
        container.setAdapter(settingsAdapter);

        PopulateView();
//        CreateWebSocketClient.sendMessage("Settings");
    }

    @SuppressLint("NotifyDataSetChanged")
    private void PopulateView() {
        for (int i = 0; i < configurationHelperList.size(); i++) {
            SettingsHelper settingsHelper = new SettingsHelper(configurationHelperList.get(i));
            settingsHelperList.add(settingsHelper);
        }
        SettingsHelper metersInfo = new SettingsHelper(settingsMetersHelperList);
        settingsHelperList.add(metersInfo);
        settingsAdapter.notifyDataSetChanged();
    }
}