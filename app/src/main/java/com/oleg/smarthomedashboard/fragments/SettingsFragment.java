package com.oleg.smarthomedashboard.fragments;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.content.res.TypedArray;
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
import com.oleg.smarthomedashboard.fragments.elements.SettingsFieldClass;
import com.oleg.smarthomedashboard.fragments.elements.SettingsFieldTypes;
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
        try {
            TypedArray data = getResources().obtainTypedArray(R.array.settings_menu);
            SettingsInfo settingsInfo;

            int cardNumber = 0, itemNumber = 0;
            while (data.getResourceId(itemNumber++, 0) != R.integer.finish) {
                ArrayList<SettingsFieldClass> settingsFieldClasses = new ArrayList<>();
                while (data.getResourceId(itemNumber, 0) != R.integer.nothing) {
                    SettingsFieldClass field;
                    if (SettingsFieldTypes.values()[data.getInt(itemNumber, 0)] == SettingsFieldTypes.METERS_CORRECTION) {
                        field = new SettingsFieldClass(
                                data.getInt(itemNumber++, 0),
                                data.getResourceId(itemNumber++, 0),
                                data.getResourceId(itemNumber++, 0),
                                data.getResourceId(itemNumber++, 0),
                                data.getResourceId(itemNumber++, 0)
                        );
                    } else {
                        field = new SettingsFieldClass(
                                data.getInt(itemNumber++, 0),
                                data.getResourceId(itemNumber++, 0),
                                data.getResourceId(itemNumber++, 0),
                                data.getResourceId(itemNumber++, 0)
                        );
                    }
                    settingsFieldClasses.add(field);
                }
                settingsInfo = new SettingsInfo(
                        data.getResourceId(cardNumber, 0),
                        settingsFieldClasses
                );
                settingsInfoList.add(settingsInfo);
                cardNumber = ++itemNumber;
            }
            settingsAdapter.notifyDataSetChanged();
            data.recycle();
        } catch (Resources.NotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}