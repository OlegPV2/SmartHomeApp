package com.oleg.smarthomedashboard.fragments.elements;

import android.annotation.SuppressLint;
import android.view.View;

import com.oleg.smarthomedashboard.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class SettingsInfo {
    private int HeadText;
    private final ArrayList<SettingsFieldClass> Field = new ArrayList<>();

    public SettingsInfo() {
    }

    public SettingsInfo(ConfigurationInfo configurationInfo) {
        this.HeadText = configurationInfo.getTitleTextID();
        for (int i = 1; i < 5; i++) {
            if (configurationInfo.getButton(i).getButtonType() == 1 &&
                    configurationInfo.getButton(i).isDimming()) {
                SettingsFieldClass field = new SettingsFieldClass(
                        SettingsFieldTypes.DIMMING.ordinal(),
                        configurationInfo.getButton(i).getButtonNote(),
                        configurationInfo.getButton(i).getDimmingSwitchID(),
                        configurationInfo.getButton(i).getDimmingSliderID());
                this.Field.add(field);
            } else if (configurationInfo.getButton(i).getButtonType() == 4) {
                SettingsFieldClass field = new SettingsFieldClass(
                        SettingsFieldTypes.TEMPERATURE.ordinal(),
                        configurationInfo.getButton(i).getButtonNote(),
                        configurationInfo.getButton(i).getWarmFloorDecreaseID(),
                        configurationInfo.getButton(i).getWarmFloorTemperatureID(),
                        configurationInfo.getButton(i).getWarmFloorIncreaseID()
                );
                this.Field.add(field);
            }
        }
    }

    @SuppressLint("ResourceType")
    public SettingsInfo(List<MetersInfo> metersInfo) {
        this.HeadText = MainActivity.getContext().getResources().getIdentifier("settings_caption_meters", "string", MainActivity.getContext().getPackageName());
        for (int i = 0; i < metersInfo.size(); i++) {
            SettingsFieldClass field = new SettingsFieldClass(
                    SettingsFieldTypes.METERS_CORRECTION.ordinal(),
                    metersInfo.get(i).getTitleTextID(),
                    metersInfo.get(i).getMeterCorrectionDecreaseID(),
                    metersInfo.get(i).getMeterCorrectionValueID(),
                    metersInfo.get(i).getMeterCorrectionIncreaseID()
            );
            this.Field.add(field);
        }
    }

    public void setHeadText(int headText) {
        HeadText = headText;
    }

    public int getHeadText() {
        return HeadText;
    }

    public int getNumberOfFields() {
        return Field.size();
    }

    public View getFields(int position) {
        return Field.get(position).getField();
    }
}
