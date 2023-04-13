package com.oleg.smarthomedashboard.fragments.elements;

import android.view.View;

import java.util.ArrayList;

public class SettingsInfo {
    private int HeadText;
    private ArrayList<SettingsFieldClass> Field;

    public SettingsInfo() {
    }

    public SettingsInfo(int headText, ArrayList<SettingsFieldClass> field) {
        HeadText = headText;
        Field = field;
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
