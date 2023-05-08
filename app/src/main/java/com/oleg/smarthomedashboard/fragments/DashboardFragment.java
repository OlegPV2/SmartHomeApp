package com.oleg.smarthomedashboard.fragments;

import static com.oleg.smarthomedashboard.MainActivity.configurationInfoList;

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

import com.oleg.smarthomedashboard.CreateWebSocketClient;
import com.oleg.smarthomedashboard.R;
import com.oleg.smarthomedashboard.fragments.elements.DashboardAdapter;
import com.oleg.smarthomedashboard.fragments.elements.DashboardInfo;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {
    private final List<DashboardInfo> dashboardInfoList = new ArrayList<>();
    private DashboardAdapter mainDashboardAdapter;
    RecyclerView container;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        container = view.findViewById(R.id.main_fragment_container);
        mainDashboardAdapter = new DashboardAdapter(dashboardInfoList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(requireActivity().getApplicationContext());
        container.setLayoutManager(mLayoutManager);
        container.setItemAnimator(new DefaultItemAnimator());
        container.setAdapter(mainDashboardAdapter);

        PopulateView();
        CreateWebSocketClient.sendMessage("Update");
    }

    @SuppressLint("NotifyDataSetChanged")
    private void PopulateView() {
        for (int i = 0; i < configurationInfoList.size(); i++) {
            DashboardInfo dashboardInfo = new DashboardInfo(configurationInfoList.get(i));
            dashboardInfoList.add(dashboardInfo);
        }
        mainDashboardAdapter.notifyDataSetChanged();
    }
}