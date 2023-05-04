package com.oleg.smarthomedashboard.fragments.elements;

import android.view.View;

public class DashboardInfo {
    private final int HeadIcon;
    private final int HeadText;
    private final boolean ShowTemperature;
    private final int TextIDTemperature;
    private final boolean ShowHumidity;
    private final int TextIDHumidity;
    private final DashboardButtonClass Button_1;
    private final DashboardButtonClass Button_2;
    private final DashboardButtonClass Button_3;
    private final DashboardButtonClass Button_4;

    public DashboardInfo(ConfigurationInfo configurationInfo) {
        HeadIcon = configurationInfo.getTitleImageID();
        HeadText = configurationInfo.getTitleTextID();
        ShowTemperature = configurationInfo.isShowTemperature();
        TextIDTemperature = configurationInfo.getTitleTemperatureID();
        ShowHumidity = configurationInfo.isShowHumidity();
        TextIDHumidity = configurationInfo.getTitleHumidityID();
        Button_1 = new DashboardButtonClass(
                configurationInfo.getButton1().getButtonType(),
                configurationInfo.getButton1().getButtonID(),
                configurationInfo.getButton1().getButtonDrawableID(),
                configurationInfo.getButton1().isButtonClickable()
        );
        Button_2 = new DashboardButtonClass(
                configurationInfo.getButton2().getButtonType(),
                configurationInfo.getButton2().getButtonID(),
                configurationInfo.getButton2().getButtonDrawableID(),
                configurationInfo.getButton2().isButtonClickable()
        );
        Button_3 = new DashboardButtonClass(
                configurationInfo.getButton3().getButtonType(),
                configurationInfo.getButton3().getButtonID(),
                configurationInfo.getButton3().getButtonDrawableID(),
                configurationInfo.getButton3().isButtonClickable()
        );
        Button_4 = new DashboardButtonClass(
                configurationInfo.getButton4().getButtonType(),
                configurationInfo.getButton4().getButtonID(),
                configurationInfo.getButton4().getButtonDrawableID(),
                configurationInfo.getButton4().isButtonClickable()
        );
    }

    public int getHeadIcon() {
        return HeadIcon;
    }

    public int getHeadText() {
        return HeadText;
    }

    public boolean getShowTemperature() {
        return ShowTemperature;
    }

    public int getTextIDTemperature() {
        return TextIDTemperature;
    }

    public boolean getShowHumidity() {
        return ShowHumidity;
    }

    public int getTextIDHumidity() {
        return TextIDHumidity;
    }

    public View getButton_1() {
        return Button_1.getButton();
    }

    public View getButton_2() {
        return Button_2.getButton();
    }

    public View getButton_3() {
        return Button_3.getButton();
    }

    public View getButton_4() {
        return Button_4.getButton();
    }
}
