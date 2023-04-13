package com.oleg.smarthomedashboard.fragments.elements;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.slider.Slider;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.oleg.smarthomedashboard.CreateWebSocketClient;
import com.oleg.smarthomedashboard.MainActivity;
import com.oleg.smarthomedashboard.R;

public class SettingsFieldClass {
    private final SettingsFieldTypes fieldType;
    private int fieldNameId;
    private int switchId;
    private int sliderId;
    private int buttonIncreaseId;
    private int buttonDecreaseId;
    private int fieldTextId;

    public SettingsFieldClass(int fieldType, int fieldNameId, int switchId, int sliderId) {
        this.fieldType = SettingsFieldTypes.values()[fieldType];
        if (this.fieldType == SettingsFieldTypes.DIMMING) {
            this.fieldNameId = fieldNameId;
            this.switchId = switchId;
            this.sliderId = sliderId;
        } else if (this.fieldType == SettingsFieldTypes.TEMPERATURE) {
            this.buttonDecreaseId = fieldNameId;
            this.fieldTextId = switchId;
            this.buttonIncreaseId = sliderId;
        }
    }

    public SettingsFieldClass(int fieldType, int fieldNameId, int buttonDecreaseId, int fieldTextId, int buttonIncreaseId) {
        this.fieldType = SettingsFieldTypes.values()[fieldType];
        this.fieldNameId = fieldNameId;
        this.buttonDecreaseId = buttonDecreaseId;
        this.fieldTextId = fieldTextId;
        this.buttonIncreaseId = buttonIncreaseId;
    }

    public View getField() {
        View field = null;
        switch (fieldType) {
            case DIMMING:
                field = View.inflate(MainActivity.getContext(), R.layout.fragment_settings_card_element_dimming, null);
                TextView head = field.findViewById(R.id.settings_dimming_head);
                head.setText(fieldNameId);
                SwitchMaterial sw = field.findViewById(R.id.dimming_switch);
                sw.setId(switchId);
                sw.setOnCheckedChangeListener(listener_switch);
                Slider sl = field.findViewById(R.id.dimming_slider);
                sl.setId(sliderId);
                sl.addOnChangeListener(listener_slider);
                break;
            case TEMPERATURE:
                field = View.inflate(MainActivity.getContext(), R.layout.fragment_settings_card_element_temperature, null);
                TextView text = field.findViewById(R.id.settings_element_temperature_text);
                text.setId(fieldTextId);
                ImageView btn = field.findViewById(R.id.settings_element_button_decrease);
                btn.setId(buttonDecreaseId);
                setOnClick(btn, text);
                btn = field.findViewById(R.id.settings_element_button_increase);
                btn.setId(buttonIncreaseId);
                setOnClick(btn, text);
                break;
            case METERS_CORRECTION:
                field = View.inflate(MainActivity.getContext(), R.layout.fragment_settings_card_element_meters_correction, null);
                TextView name = field.findViewById(R.id.settings_element_meters_text);
                name.setText(fieldNameId);
                name = field.findViewById(R.id.settings_element_meters_value);
                name.setId(fieldTextId);
                ImageButton incDec = field.findViewById(R.id.settings_element_meters_decrease);
                incDec.setId(buttonDecreaseId);
                setOnClick(incDec, name);
                incDec = field.findViewById(R.id.settings_element_meters_increase);
                incDec.setId(buttonIncreaseId);
                setOnClick(incDec, name);
                break;
        }
        return field;
    }


    private final CompoundButton.OnCheckedChangeListener listener_switch = (compoundButton, b) -> {
        String[] cmd = compoundButton.getResources().getResourceEntryName(compoundButton.getId()).split("_");
        Log.d("SwitchListener", cmd[1] + ":" + b);
    };

    private final Slider.OnChangeListener listener_slider = (slider, value, fromUser) -> {
        String[] cmd = slider.getResources().getResourceEntryName(slider.getId()).split("_");
        CreateWebSocketClient.sendMessage("s:" + cmd[1] + ":dim-set:" + (200 - (int) slider.getValue()));
    };

    @SuppressLint("NonConstantResourceId")
    private void setOnClick(final View view, final TextView textView) {
        view.setOnClickListener(view1 -> {
            int val;
            Object tag = view1.getTag();
            if (view1.getResources().getString(R.string.settings_button_tag_increase).equals(tag)) {
                val = Integer.parseInt((String) textView.getText());
                val++;
                textView.setText(String.valueOf(val));
            } else if (view1.getResources().getString(R.string.settings_button_tag_decrease).equals(tag)) {
                val = Integer.parseInt((String) textView.getText());
                val--;
                textView.setText(String.valueOf(val));
            }
        });
    }
}
