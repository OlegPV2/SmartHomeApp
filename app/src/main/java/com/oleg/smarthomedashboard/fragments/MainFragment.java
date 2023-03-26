package com.oleg.smarthomedashboard.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oleg.smarthomedashboard.R;

public class MainFragment extends Fragment {

    public MainFragment() {
        // Required empty public constructor
    }

    private int getBckColor(String s) {
        switch (s.charAt(0)) {
            case 'l':
            case 't':
                return R.color.amber_A200;
            case 'w':
                return R.color.amber_900;
            case 'a':
                return R.color.green_A700;
            case 'f':
                return R.color.blue_400;
        }
        return R.color.red_800;
    }

    private final View.OnClickListener listener = view -> {
        if (view.getBackground() == null) {
            view.setBackgroundColor(getResources().getColor(getBckColor((String) view.getTag()), requireActivity().getApplication().getTheme()));
        } else {
            view.setBackground(null);
        }
        // TODO: Send new state to server
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        String[] buttonID = this.getResources().getStringArray(R.array.dashboard_buttons);
        for (String s : buttonID) {
            @SuppressLint("DiscouragedApi") View btn = view.findViewById(getResources().getIdentifier(s, "id", requireActivity().getPackageName()));
            btn.setTag(s);
            btn.setOnClickListener(listener);
        }
        // TODO: update states of buttons
    }
}