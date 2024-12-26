package com.oleg.smarthomedashboard.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;

import com.oleg.smarthomedashboard.MainActivity;

public class SettingsMetersHelper {
    private final int titleTextID;
    private final String meterCorrectionDecreaseID;
    private final String meterCorrectionValueID;
    private final String meterCorrectionIncreaseID;

    @SuppressLint("DiscouragedApi")
    public SettingsMetersHelper(String titleTextID, String meterCorrectionDecreaseID, String meterCorrectionValueID, String meterCorrectionIncreaseID) {
        Context context = MainActivity.getContext();
        Resources resources = context.getResources();
        this.titleTextID = resources.getIdentifier(titleTextID, "string", context.getPackageName());
        this.meterCorrectionDecreaseID = meterCorrectionDecreaseID;
        this.meterCorrectionValueID = meterCorrectionValueID;
        this.meterCorrectionIncreaseID = meterCorrectionIncreaseID;
    }

    public int getTitleTextID() {
        return titleTextID;
    }

    public String getMeterCorrectionDecreaseID() {
        return meterCorrectionDecreaseID;
    }

    public String getMeterCorrectionValueID() {
        return meterCorrectionValueID;
    }

    public String getMeterCorrectionIncreaseID() {
        return meterCorrectionIncreaseID;
    }
}
