package com.oleg.smarthomedashboard.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;

import com.oleg.smarthomedashboard.MainActivity;

public class ConfigurationButtonsHelper {
    private final int buttonType;
    private final String buttonID;
    private final int buttonDrawableID;
    private final boolean buttonClickable;
    private boolean dimming;
    private String dimmingSwitchID;
    private String dimmingSliderID;
    private int warmFloorPreset;
    private String warmFloorDecreaseID;
    private String warmFloorTemperatureID;
    private String warmFloorIncreaseID;
    private final Context context = MainActivity.getContext();
    private final Resources resources = context.getResources();
    private final int buttonNote;

    @SuppressLint("DiscouragedApi")
    public ConfigurationButtonsHelper(int buttonType, String buttonID, String buttonDrawableID, boolean buttonClickable, String buttonNote) {
        this.buttonType = buttonType;
        this.buttonID = buttonID;
        this.buttonDrawableID = resources.getIdentifier(buttonDrawableID, "drawable", context.getPackageName());
        this.buttonClickable = buttonClickable;
        this.buttonNote = resources.getIdentifier(buttonNote, "string", context.getPackageName());
    }

    @SuppressLint("DiscouragedApi")
    public ConfigurationButtonsHelper(int buttonType, String buttonID, String buttonDrawableID, boolean buttonClickable, boolean dimming, String dimmingSwitchID, String dimmingSliderID, String buttonNote) {
        this.buttonType = buttonType;
        this.buttonID = buttonID;
        this.buttonDrawableID = resources.getIdentifier(buttonDrawableID, "drawable", context.getPackageName());
        this.buttonClickable = buttonClickable;
        this.dimming = dimming;
        this.dimmingSwitchID = dimmingSwitchID;
        this.dimmingSliderID = dimmingSliderID;
        this.buttonNote = resources.getIdentifier(buttonNote, "string", context.getPackageName());
    }

    @SuppressLint("DiscouragedApi")
    public ConfigurationButtonsHelper(int buttonType, String buttonID, String buttonDrawableID, boolean buttonClickable, int warmFloorPreset, String warmFloorDecreaseID, String warmFloorTemperatureID, String warmFloorIncreaseID, String buttonNote) {
        this.buttonType = buttonType;
        this.buttonID = buttonID;
        this.buttonDrawableID = resources.getIdentifier(buttonDrawableID, "id", context.getPackageName());
        this.buttonClickable = buttonClickable;
        this.warmFloorPreset = warmFloorPreset;
        this.warmFloorDecreaseID = warmFloorDecreaseID;
        this.warmFloorTemperatureID = warmFloorTemperatureID;
        this.warmFloorIncreaseID = warmFloorIncreaseID;
        this.buttonNote = resources.getIdentifier(buttonNote, "string", context.getPackageName());
    }

    public int getButtonType() {
        return this.buttonType;
    }

    public String getButtonID() {
        return this.buttonID;
    }

    public int getButtonDrawableID() {
        return this.buttonDrawableID;
    }

    public boolean isButtonClickable() {
        return this.buttonClickable;
    }

    public boolean isDimming() {
        return this.dimming;
    }

    public String getDimmingSwitchID() {
        return this.dimmingSwitchID;
    }

    public String getDimmingSliderID() {
        return this.dimmingSliderID;
    }

    public int getWarmFloorPreset() {
        return this.warmFloorPreset;
    }

    public String getWarmFloorDecreaseID() {
        return this.warmFloorDecreaseID;
    }

    public String getWarmFloorTemperatureID() {
        return this.warmFloorTemperatureID;
    }

    public String getWarmFloorIncreaseID() {
        return this.warmFloorIncreaseID;
    }

    public int getButtonNote() {
        return this.buttonNote;
    }
}
