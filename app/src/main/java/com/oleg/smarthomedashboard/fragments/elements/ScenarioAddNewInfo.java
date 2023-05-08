package com.oleg.smarthomedashboard.fragments.elements;

import android.view.View;

public class ScenarioAddNewInfo {
    private final int HeadText;
    private final ScenarioAddNewButtonClass Button_1;
    private final ScenarioAddNewButtonClass Button_2;
    private final ScenarioAddNewButtonClass Button_3;
    private final ScenarioAddNewButtonClass Button_4;

    public ScenarioAddNewInfo(
            int headText,
            ScenarioAddNewButtonClass button_1,
            ScenarioAddNewButtonClass button_2,
            ScenarioAddNewButtonClass button_3,
            ScenarioAddNewButtonClass button_4) {
        HeadText = headText;
        Button_1 = button_1;
        Button_2 = button_2;
        Button_3 = button_3;
        Button_4 = button_4;
    }

    public int getHeadText() {
        return HeadText;
    }

    public View getButton_1() {
        return Button_1.getButton(false);
    }

    public View getButton_2() {
        return Button_2.getButton(false);
    }

    public View getButton_3() {
        return Button_3.getButton(false);
    }

    public View getButton_4() {
        return Button_4.getButton(false);
    }
}
