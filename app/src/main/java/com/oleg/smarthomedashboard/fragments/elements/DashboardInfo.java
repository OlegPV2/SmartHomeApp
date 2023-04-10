package com.oleg.smarthomedashboard.fragments.elements;

import android.view.View;

public class DashboardInfo {
    private int HeadIcon;
    private int HeadText;
    private boolean ShowTemperature;
    private int TextIDTemperature;
    private boolean ShowHumidity;
    private int TextIDHumidity;
    private ButtonClass Button_1;
    private ButtonClass Button_2;
    private ButtonClass Button_3;
    private ButtonClass Button_4;

    public DashboardInfo() {
    }

    public DashboardInfo(int headIcon,
                      int headText,
                      boolean showTemperature,
                      int textIDTemperature,
                      boolean showHumidity,
                      int textIDHumidity,
                      ButtonClass button_1,
                      ButtonClass button_2,
                      ButtonClass button_3,
                      ButtonClass button_4) {
        HeadIcon = headIcon;
        HeadText = headText;
        ShowTemperature = showTemperature;
        TextIDTemperature = textIDTemperature;
        ShowHumidity = showHumidity;
        TextIDHumidity = textIDHumidity;
        Button_1 = button_1;
        Button_2 = button_2;
        Button_3 = button_3;
        Button_4 = button_4;
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
