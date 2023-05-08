package com.oleg.smarthomedashboard.fragments.elements;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;

import com.oleg.smarthomedashboard.MainActivity;

public class ConfigurationInfo {
    private final int titleTextID;
    private final int titleImageID;
    private final boolean showTemperature;
    private final int titleTemperatureID;
    private final boolean showHumidity;
    private final int titleHumidityID;
    private final ConfigurationButtonsInfo button1;
    private final ConfigurationButtonsInfo button2;
    private final ConfigurationButtonsInfo button3;
    private final ConfigurationButtonsInfo button4;
    Context context = MainActivity.getContext();
    Resources resources = context.getResources();

    @SuppressLint("DiscouragedApi")
    public ConfigurationInfo(String titleTextID,
                             String titleImageID,
                             boolean showTemperature,
                             String titleTemperatureID,
                             boolean showHumidity,
                             String titleHumidityID,
                             ConfigurationButtonsInfo button1,
                             ConfigurationButtonsInfo button2,
                             ConfigurationButtonsInfo button3,
                             ConfigurationButtonsInfo button4) {
        this.titleTextID = resources.getIdentifier(titleTextID, "string", context.getPackageName());
        this.titleImageID = resources.getIdentifier(titleImageID, "drawable", context.getPackageName());
        this.showTemperature = showTemperature;
        this.titleTemperatureID = resources.getIdentifier(titleTemperatureID, "id", context.getPackageName());
        this.showHumidity = showHumidity;
        this.titleHumidityID = resources.getIdentifier(titleHumidityID, "id", context.getPackageName());
        this.button1 = button1;
        this.button2 = button2;
        this.button3 = button3;
        this.button4 = button4;
    }

    public int getTitleTextID() {
        return this.titleTextID;
    }

    public int getTitleImageID() {
        return this.titleImageID;
    }

    public boolean isShowTemperature() {
        return this.showTemperature;
    }

    public int getTitleTemperatureID() {
        return this.titleTemperatureID;
    }

    public boolean isShowHumidity() {
        return this.showHumidity;
    }

    public int getTitleHumidityID() {
        return this.titleHumidityID;
    }

    public ConfigurationButtonsInfo getButton1() {
        return this.button1;
    }

    public ConfigurationButtonsInfo getButton2() {
        return this.button2;
    }

    public ConfigurationButtonsInfo getButton3() {
        return this.button3;
    }

    public ConfigurationButtonsInfo getButton4() {
        return this.button4;
    }

    public ConfigurationButtonsInfo getButton(int number) {
        switch (number) {
            case 1:
                return this.button1;
            case 2:
                return this.button2;
            case 3:
                return this.button3;
            case 4:
                return this.button4;
        }
        return null;
    }
}
