package com.oleg.smarthomedashboard.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oleg.smarthomedashboard.CreateWebSocketClient;
import com.oleg.smarthomedashboard.MainActivity;
import com.oleg.smarthomedashboard.R;

public class MetersFragment extends Fragment {

    public MetersFragment() {
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
        return inflater.inflate(R.layout.fragment_meters, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        CreateWebSocketClient.sendMessage(((MainActivity) requireActivity()), "Meters");
    }
}