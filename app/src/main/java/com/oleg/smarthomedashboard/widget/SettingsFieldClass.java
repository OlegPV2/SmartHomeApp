package com.oleg.smarthomedashboard.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.slider.Slider;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.oleg.smarthomedashboard.WSClient;
import com.oleg.smarthomedashboard.MainActivity;
import com.oleg.smarthomedashboard.R;
import com.oleg.smarthomedashboard.model.SettingsFieldTypes;

public class SettingsFieldClass {
    private final SettingsFieldTypes fieldType;
    private int fieldNameId;
    private String switchId;
    private String sliderId;
    private int buttonIncreaseId;
    private int buttonDecreaseId;
    private int fieldTextId;
    private static boolean isSwitchListenerEnabled = true;
    private static boolean isSlideListenerEnabled = true;

    public SettingsFieldClass(int fieldType, int fieldNameId, String switchId, String sliderId) {
        this.fieldType = SettingsFieldTypes.values()[fieldType];
        if (this.fieldType == SettingsFieldTypes.DIMMING) {
            this.fieldNameId = fieldNameId;
            this.switchId = switchId;
            this.sliderId = sliderId;
        } else if (this.fieldType == SettingsFieldTypes.TEMPERATURE) {
            Context context = MainActivity.getContext();
            Resources resources = context.getResources();
            this.buttonDecreaseId = fieldNameId;
            this.fieldTextId = resources.getIdentifier(switchId, "id", context.getPackageName());
            this.buttonIncreaseId = resources.getIdentifier(sliderId, "id", context.getPackageName());
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
                sw.setTag(switchId);
                sw.setOnCheckedChangeListener(listener_switch);
                Slider sl = field.findViewById(R.id.dimming_slider);
                sl.setTag(sliderId);
                sl.addOnChangeListener(listener_slider);
                break;
            case TEMPERATURE:
                field = View.inflate(MainActivity.getContext(), R.layout.fragment_settings_card_element_temperature, null);
                TextView text = field.findViewById(R.id.settings_element_temperature_text);
                text.setTag(fieldTextId);
                ImageView btn = field.findViewById(R.id.settings_element_button_decrease);
                btn.setTag(buttonDecreaseId);
                setOnClick(btn, text);
                btn = field.findViewById(R.id.settings_element_button_increase);
                btn.setTag(buttonIncreaseId);
                setOnClick(btn, text);
                break;
            case METERS_CORRECTION:
                field = View.inflate(MainActivity.getContext(), R.layout.fragment_settings_card_element_meters_correction, null);
                TextView name = field.findViewById(R.id.settings_element_meters_text);
                name.setText(fieldNameId);
                EditText nameEdit = field.findViewById(R.id.settings_element_meters_value);
                nameEdit.setTag(fieldTextId);
                nameEdit.setOnEditorActionListener((textView, i, keyEvent) -> {
                    boolean handled = false;
                    if (i == EditorInfo.IME_ACTION_SEND) {
                        String[] cmd = textView.getTag().toString().split("_");
                        WSClient.sendMessage("s:" + cmd[2] + ":" + textView.getText());
                        handled = true;
                    }
                    return handled;
                });
                ImageView incDec = field.findViewById(R.id.settings_element_meters_decrease);
                incDec.setTag(buttonDecreaseId);
                setOnClick(incDec, nameEdit);
                incDec = field.findViewById(R.id.settings_element_meters_increase);
                incDec.setTag(buttonIncreaseId);
                setOnClick(incDec, nameEdit);
                break;
        }
        return field;
    }

    private final CompoundButton.OnCheckedChangeListener listener_switch = (compoundButton, b) -> {
        if (isSwitchListenerEnabled) {
            String[] cmd = compoundButton.getTag().toString().split("_");
            Slider sl = compoundButton.getRootView().findViewWithTag("dimming_" + cmd[1] + "_slider");
            sl.setEnabled(b);
            WSClient.sendMessage("s:" + cmd[1] + ":dimming:" + (b ? 1 : 0));
        }
    };

    private final Slider.OnChangeListener listener_slider = (slider, value, fromUser) -> {
        if (isSlideListenerEnabled) {
            String[] cmd = slider.getTag().toString().split("_");
            WSClient.sendMessage("s:" + cmd[1] + ":dim-set:" + (200 - (int) slider.getValue()));
        }
    };

    @SuppressLint("NonConstantResourceId")
    private void setOnClick(final View view, final TextView textView) {
        view.setOnClickListener(view1 -> {
            double val;
            Object tag = view1.getTag();
            if (view1.getResources().getString(R.string.settings_button_tag_increase).equals(tag)) {
                val = Double.parseDouble(textView.getText().toString());
                val++;
                textView.setText(String.valueOf(val));
            } else if (view1.getResources().getString(R.string.settings_button_tag_decrease).equals(tag)) {
                val = Double.parseDouble(textView.getText().toString());
                val--;
                textView.setText(String.valueOf(val));
            }
            String[] cmd = textView.getTag().toString().split("_");
            WSClient.sendMessage("s:" + cmd[2] + ":" + textView.getText());
        });
    }

    public static void setSlideListenerEnabled(boolean slideListenerEnabled) {
        isSlideListenerEnabled = slideListenerEnabled;
    }

    public static void setSwitchListenerEnabled(boolean switchListenerEnabled) {
        isSwitchListenerEnabled = switchListenerEnabled;
    }
}
