package com.oleg.smarthomedashboard.helper;

import android.view.View;

import com.oleg.smarthomedashboard.widget.DashboardButtonClass;

public class DashboardHelper {
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

    public DashboardHelper(ConfigurationHelper configurationHelper) {
        HeadIcon = configurationHelper.getTitleImageID();
        HeadText = configurationHelper.getTitleTextID();
        ShowTemperature = configurationHelper.isShowTemperature();
        TextIDTemperature = configurationHelper.getTitleTemperatureID();
        ShowHumidity = configurationHelper.isShowHumidity();
        TextIDHumidity = configurationHelper.getTitleHumidityID();
        Button_1 = new DashboardButtonClass(
                configurationHelper.getButton1().getButtonType(),
                configurationHelper.getButton1().getButtonID(),
                configurationHelper.getButton1().getButtonDrawableID(),
                configurationHelper.getButton1().isButtonClickable()
        );
        Button_2 = new DashboardButtonClass(
                configurationHelper.getButton2().getButtonType(),
                configurationHelper.getButton2().getButtonID(),
                configurationHelper.getButton2().getButtonDrawableID(),
                configurationHelper.getButton2().isButtonClickable()
        );
        Button_3 = new DashboardButtonClass(
                configurationHelper.getButton3().getButtonType(),
                configurationHelper.getButton3().getButtonID(),
                configurationHelper.getButton3().getButtonDrawableID(),
                configurationHelper.getButton3().isButtonClickable()
        );
        Button_4 = new DashboardButtonClass(
                configurationHelper.getButton4().getButtonType(),
                configurationHelper.getButton4().getButtonID(),
                configurationHelper.getButton4().getButtonDrawableID(),
                configurationHelper.getButton4().isButtonClickable()
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
