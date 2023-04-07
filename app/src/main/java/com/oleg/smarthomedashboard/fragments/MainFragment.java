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
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.oleg.smarthomedashboard.CreateWebSocketClient;
import com.oleg.smarthomedashboard.MainActivity;
import com.oleg.smarthomedashboard.R;

public class MainFragment extends Fragment {

    public MainFragment() {
        // Required empty public constructor
    }

    private int getBckColor(String s) {
        switch (s.charAt(0)) {
            case 'l':
            case 'b':
                return R.drawable.rounded_corners_yellow;
            case 'w':
                return R.drawable.rounded_corners_amber;
            case 'a':
                return R.drawable.rounded_corners_green;
            case 'f':
                return R.drawable.rounded_corners_blue;
        }
        return R.color.error;
    }

    private final View.OnClickListener listener = view -> {
//        if (CreateWebSocketClient.getReadyState() != ReadyState.OPEN) {
            if (view.getBackground() == null) {
                String t = getResources().getResourceEntryName(view.getId());
                view.setBackground(ResourcesCompat.getDrawable(getResources(), getBckColor(t), requireActivity().getApplication().getTheme()));
                if (t.charAt(0) == 'w') {
                    View v = ((ViewGroup) view).getChildAt(1);
                    View tv = ((LinearLayout) v).getChildAt(0);
                    ((TextView) tv).setTextColor(getResources().getColor(R.color.white, requireActivity().getTheme()));
                    tv = ((LinearLayout) v).getChildAt(1);
                    ((TextView) tv).setTextColor(getResources().getColor(R.color.white, requireActivity().getTheme()));
                }
            } else {
                view.setBackground(null);
                if (getResources().getResourceEntryName(view.getId()).charAt(0) == 'w') {
                    View v = ((ViewGroup) view).getChildAt(1);
                    View tv = ((LinearLayout) v).getChildAt(0);
                    ((TextView) tv).setTextColor(getResources().getColor(R.color.text_low, requireActivity().getTheme()));
                    tv = ((LinearLayout) v).getChildAt(1);
                    ((TextView) tv).setTextColor(getResources().getColor(R.color.text_low, requireActivity().getTheme()));
                }
            }
//        }
        CreateWebSocketClient.sendMessage(((MainActivity) requireActivity()), view);
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
            @SuppressLint("DiscouragedApi")
            View btn = view.findViewById(getResources().getIdentifier(s, "id", requireActivity().getPackageName()));
            btn.setOnClickListener(listener);
        }
        CreateWebSocketClient.sendMessage(((MainActivity) requireActivity()), "Update");
    }
}