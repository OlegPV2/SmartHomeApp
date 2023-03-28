package com.oleg.smarthomedashboard.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.oleg.smarthomedashboard.R;

public class MainFragment extends Fragment {

    public MainFragment() {
        // Required empty public constructor
    }

    private int getBckColor(String s) {
        switch (s.charAt(0)) {
            case 'l':
            case 't':
                return R.color.background_button_light;
            case 'w':
                return R.color.background_button_wf;
            case 'a':
                return R.color.background_button_auto;
            case 'f':
                return R.color.background_button_fan;
        }
        return R.color.error;
    }

    private final View.OnClickListener listener = view -> {
        if (view.getBackground() == null) {
            view.setBackgroundColor(getResources().getColor(getBckColor((String) view.getTag()), requireActivity().getApplication().getTheme()));
            if (((String) view.getTag()).charAt(0) == 'w') {
                View v = ((ViewGroup) view).getChildAt(1);
                View tv = ((LinearLayout) v).getChildAt(0);
                ((TextView) tv).setTextColor(getResources().getColor(R.color.white, requireActivity().getTheme()));
                tv = ((LinearLayout) v).getChildAt(1);
                ((TextView) tv).setTextColor(getResources().getColor(R.color.white, requireActivity().getTheme()));
            }
        } else {
            view.setBackground(null);
            if (((String) view.getTag()).charAt(0) == 'w') {
                View v = ((ViewGroup) view).getChildAt(1);
                View tv = ((LinearLayout) v).getChildAt(0);
                ((TextView) tv).setTextColor(getResources().getColor(R.color.text_low, requireActivity().getTheme()));
                tv = ((LinearLayout) v).getChildAt(1);
                ((TextView) tv).setTextColor(getResources().getColor(R.color.text_low, requireActivity().getTheme()));
            }
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