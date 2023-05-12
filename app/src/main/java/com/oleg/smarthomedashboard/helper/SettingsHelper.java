package com.oleg.smarthomedashboard.helper;

import android.annotation.SuppressLint;
import android.view.View;

import com.oleg.smarthomedashboard.MainActivity;
import com.oleg.smarthomedashboard.model.SettingsFieldTypes;
import com.oleg.smarthomedashboard.widget.SettingsFieldClass;

import java.util.ArrayList;
import java.util.List;

public class SettingsHelper {
    private int HeadText;
    private final ArrayList<SettingsFieldClass> Field = new ArrayList<>();

    public SettingsHelper() {
    }

    public SettingsHelper(ConfigurationHelper configurationHelper) {
        this.HeadText = configurationHelper.getTitleTextID();
        for (int i = 1; i < 5; i++) {
            if (configurationHelper.getButton(i).getButtonType() == 1 &&
                    configurationHelper.getButton(i).isDimming()) {
                SettingsFieldClass field = new SettingsFieldClass(
                        SettingsFieldTypes.DIMMING.ordinal(),
                        configurationHelper.getButton(i).getButtonNote(),
                        configurationHelper.getButton(i).getDimmingSwitchID(),
                        configurationHelper.getButton(i).getDimmingSliderID());
                this.Field.add(field);
            } else if (configurationHelper.getButton(i).getButtonType() == 4) {
                SettingsFieldClass field = new SettingsFieldClass(
                        SettingsFieldTypes.TEMPERATURE.ordinal(),
                        configurationHelper.getButton(i).getButtonNote(),
                        configurationHelper.getButton(i).getWarmFloorDecreaseID(),
                        configurationHelper.getButton(i).getWarmFloorTemperatureID(),
                        configurationHelper.getButton(i).getWarmFloorIncreaseID()
                );
                this.Field.add(field);
            }
        }
    }

    @SuppressLint("ResourceType")
    public SettingsHelper(List<SettingsMetersHelper> settingsMetersHelper) {
        this.HeadText = MainActivity.getContext().getResources().getIdentifier("settings_caption_meters", "string", MainActivity.getContext().getPackageName());
        for (int i = 0; i < settingsMetersHelper.size(); i++) {
            SettingsFieldClass field = new SettingsFieldClass(
                    SettingsFieldTypes.METERS_CORRECTION.ordinal(),
                    settingsMetersHelper.get(i).getTitleTextID(),
                    settingsMetersHelper.get(i).getMeterCorrectionDecreaseID(),
                    settingsMetersHelper.get(i).getMeterCorrectionValueID(),
                    settingsMetersHelper.get(i).getMeterCorrectionIncreaseID()
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
