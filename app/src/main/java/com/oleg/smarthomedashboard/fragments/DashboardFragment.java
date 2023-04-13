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

import com.oleg.smarthomedashboard.CreateWebSocketClient;
import com.oleg.smarthomedashboard.R;
import com.oleg.smarthomedashboard.fragments.elements.DashboardButtonClass;
import com.oleg.smarthomedashboard.fragments.elements.DashboardInfo;
import com.oleg.smarthomedashboard.fragments.elements.MainDashboardAdapter;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {
    private final List<DashboardInfo> dashboardInfoList = new ArrayList<>();
    private MainDashboardAdapter mainDashboardAdapter;
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
        mainDashboardAdapter = new MainDashboardAdapter(dashboardInfoList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(requireActivity().getApplicationContext());
        container.setLayoutManager(mLayoutManager);
        container.setItemAnimator(new DefaultItemAnimator());
        container.setAdapter(mainDashboardAdapter);

        PopulateView();
        CreateWebSocketClient.sendMessage("Update");
    }

    @SuppressLint("NotifyDataSetChanged")
    private void PopulateView() {
        try {
            TypedArray data = getResources().obtainTypedArray(R.array.dashboard_menu);
            for (int i = 0; i < data.length(); i++) {
                DashboardInfo dashboardInfo = new DashboardInfo(
                        data.getResourceId(i++, 0),
                        data.getResourceId(i++, 0),
                        data.getBoolean(i++, false),
                        data.getResourceId(i++, 0),
                        data.getBoolean(i++, false),
                        data.getResourceId(i++, 0),
                        new DashboardButtonClass(
                                this.getContext(),
                                data.getInt(i++, 0),
                                data.getResourceId(i++, 0),
                                data.getResourceId(i++, 0),
                                data.getBoolean(i++, false)),
                        new DashboardButtonClass(
                                this.getContext(),
                                data.getInt(i++, 0),
                                data.getResourceId(i++, 0),
                                data.getResourceId(i++, 0),
                                data.getBoolean(i++, false)),
                        new DashboardButtonClass(
                                this.getContext(),
                                data.getInt(i++, 0),
                                data.getResourceId(i++, 0),
                                data.getResourceId(i++, 0),
                                data.getBoolean(i++, false)),
                        new DashboardButtonClass(
                                this.getContext(),
                                data.getInt(i++, 0),
                                data.getResourceId(i++, 0),
                                data.getResourceId(i++, 0),
                                data.getBoolean(i, false))
                );
                dashboardInfoList.add(dashboardInfo);
            }
            data.recycle();
            mainDashboardAdapter.notifyDataSetChanged();
        } catch (Resources.NotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}