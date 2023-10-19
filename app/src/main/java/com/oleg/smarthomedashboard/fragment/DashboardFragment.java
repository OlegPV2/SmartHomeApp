package com.oleg.smarthomedashboard.fragment;

import static com.oleg.smarthomedashboard.MainActivity.FIRST_START;
import static com.oleg.smarthomedashboard.MainActivity.getConfigurationHelperList;

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
import com.oleg.smarthomedashboard.adapter.DashboardAdapter;
import com.oleg.smarthomedashboard.helper.DashboardHelper;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {
    private final List<DashboardHelper> dashboardHelperList = new ArrayList<>();
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
        mainDashboardAdapter = new DashboardAdapter(dashboardHelperList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(requireActivity().getApplicationContext());
        container.setLayoutManager(mLayoutManager);
        container.setItemAnimator(new DefaultItemAnimator());
        container.setAdapter(mainDashboardAdapter);

        PopulateView();
        if (!FIRST_START) CreateWebSocketClient.sendMessage("Update");
    }

    @SuppressLint("NotifyDataSetChanged")
    private void PopulateView() {
        for (int i = 0; i < getConfigurationHelperList().size(); i++) {
            DashboardHelper dashboardHelper = new DashboardHelper(getConfigurationHelperList().get(i));
            dashboardHelperList.add(dashboardHelper);
        }
        mainDashboardAdapter.notifyDataSetChanged();
    }
}