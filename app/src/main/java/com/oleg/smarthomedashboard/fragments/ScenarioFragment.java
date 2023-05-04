package com.oleg.smarthomedashboard.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.button.MaterialButton;
import com.oleg.smarthomedashboard.R;

public class ScenarioFragment extends Fragment {

    public ScenarioFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scenario, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MaterialButton btn = view.findViewById(R.id.scenario_01);
        btn.setOnClickListener(view1 -> {
            Log.d("ScenarioButton", String.valueOf(view1.getId()));
        });
        btn = view.findViewById(R.id.scenario_new);
        btn.setOnClickListener(view1 -> {
            Log.d("ScenarioButton", "New scenario");
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
            transaction.replace(R.id.container, new ScenarioAddNew());
            transaction.commit();
        });
    }
}