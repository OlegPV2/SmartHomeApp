package com.oleg.smarthomedashboard.fragments;

import static java.lang.String.valueOf;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.material.slider.Slider;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.oleg.smarthomedashboard.CreateWebSocketClient;
import com.oleg.smarthomedashboard.MainActivity;
import com.oleg.smarthomedashboard.R;

public class SettingsFragment extends Fragment {

    public SettingsFragment() {
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
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    private final CompoundButton.OnCheckedChangeListener listener_switch = (compoundButton, b) -> {
        String[] cmd = getResources().getResourceEntryName(compoundButton.getId()).split("_");
        Toast.makeText(getActivity(), cmd[1] + ":" + b, Toast.LENGTH_SHORT).show();
    };

    private final Slider.OnChangeListener listener_slider = new Slider.OnChangeListener() {
        @Override
        public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
            String[] cmd = getResources().getResourceEntryName(slider.getId()).split("_");
            CreateWebSocketClient.sendMessage(((MainActivity) requireActivity()), "s:" + cmd[1] + ":dim-set:" + (200 - (int)slider.getValue()));
//            Log.d("Slider","s:" + cmd[1] + ":dim-set:" + (int)slider.getValue());
//            Toast.makeText(getActivity(), "s:" + cmd[1] + ":dim-set:" + (int)slider.getValue(), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        String[] switchID = this.getResources().getStringArray(R.array.settings_switch);
        for (String s : switchID) {
            SwitchMaterial swch = view.findViewById(getResources().getIdentifier(s, "id", requireActivity().getPackageName()));
            swch.setOnCheckedChangeListener(listener_switch);
        }
        String[] sliderID = this.getResources().getStringArray(R.array.settings_slider);
        for (String s : sliderID) {
            Slider sldr = view.findViewById(getResources().getIdentifier(s, "id", requireActivity().getPackageName()));
            sldr.addOnChangeListener(listener_slider);
        }
//        ((MainActivity) requireActivity()).sendMessage("Settings");
    }
}